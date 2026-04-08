package com.example.foodchooser.controller;

import com.example.foodchooser.captcha.CaptchaCache;
import com.example.foodchooser.entity.User;
import com.example.foodchooser.service.UserService;
import com.example.foodchooser.utils.JwtUtils;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users") // 给所有接口加一个统一前缀，比如 /users
public class UserController {

    @Autowired
    private UserService userService;

    // ================= 新增：获取验证码接口 =================
    @GetMapping("/captcha")
    public Map<String, Object> getCaptcha(HttpServletResponse response)
    {
        Map<String, Object> result = new HashMap<>();
        try
        {
            // 1. 创建验证码对象（宽130，高48，4位字符）
            SpecCaptcha captcha = new SpecCaptcha(130, 48, 4
            );
            // 2. 设置验证码类型为数字和字母混合
            captcha.setCharType(Captcha.TYPE_DEFAULT);
            // 3. 生成唯一的key（用于存储和验证）
            String key = UUID.randomUUID().toString().replace("-", ""
            );
            // 4. 将验证码存入缓存
            String code = captcha.text().toLowerCase(); // 转为小写方便比较
            CaptchaCache.put(key, code);
            // 5. 设置响应头（防止浏览器缓存）
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            // 6. 返回Base64图片和key给前端
            result.put("code", 200);
            result.put("message", "验证码生成成功");
            result.put("key", key);
            result.put("image", captcha.toBase64()); // 返回Base64格式图片
            return result;
        }
        catch
        (Exception e) {
            result.put("code", 500);
            result.put("message", "验证码生成失败：" + e.getMessage());
            return result;
        }
    }

    // 注册接口
    // 访问方式：POST http://localhost:8080/users/register
    // 前端需要在请求体里发送 JSON 数据，例如：{"username": "zhangsan", "password": "123456"}
    // ================= 修改后的注册接口 =================
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> requestData) {
        Map<String, Object> result = new HashMap<>();

        // 1. 获取前端传来的参数
        String username = requestData.get("username");
        String password = requestData.get("password");
        String captchaKey = requestData.get("captchaKey");    // 验证码key
        String captchaCode = requestData.get("captchaCode");   // 用户输入的验证码

        // 2. 验证验证码
        if (!StringUtils.hasText(captchaKey) || !StringUtils.hasText(captchaCode)) {
            result.put("code", 400);
            result.put("message", "请输入验证码");
            return result;
        }

        // 3. 使用CaptchaCache验证验证码
        if (!CaptchaCache.checkAndRemove(captchaKey, captchaCode.toLowerCase())) {
            result.put("code", 400);
            result.put("message", "验证码错误或已过期");
            return result;
        }

        // 4. 验证用户名和密码
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            result.put("code", 400);
            result.put("message", "用户名和密码不能为空");
            return result;
        }

        // 5. 调用服务层注册
        boolean success = userService.register(username, password);
        if (success) {
            result.put("code", 200);
            result.put("message", "注册成功！");
        } else {
            result.put("code", 400);
            result.put("message", "注册失败：用户名已存在！");
        }

        return result;
    }

    // 登录接口
    // 访问方式：POST http://localhost:8080/users/login
    // 前端同样发送 JSON 数据
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> requestData) {
        Map<String, Object> result = new HashMap<>(); // 用来装最终返回给前端的JSON数据
        // 【改动2】：把暗号和答案从请求里掏出来
        String username = requestData.get("username");
        String password = requestData.get("password");
        String captchaKey = requestData.get("captchaKey");
        String captchaCode = requestData.get("captchaCode");

        // 【改动3：防刷第一道防线】先查验证码！不对直接打回！
        if (captchaKey == null || captchaCode == null || !CaptchaCache.checkAndRemove(captchaKey, captchaCode.toLowerCase())) {
            result.put("code", 400);
            result.put("message", "验证码错误或已过期！");
            return result;
        }
        User loginUser = userService.login(username, password);
        if (loginUser != null) {
            // 【核心新增】：密码正确，调用工具类生成 JWT Token
            String token = JwtUtils.generateToken(loginUser.getUsername());
            // 把 Token 和 提示信息 塞进 Map 里
            result.put("code", 200);           // 状态码 200 代表成功
            result.put("message", "登录成功！欢迎 " + loginUser.getUsername());
            result.put("token", token);        // 【最重要的一步】把 Token 传给前端
            return result;
        }
        else {
            // 登录失败不需要生成 Token
            result.put("code", 400);           // 状态码 400 代表失败
            result.put("message", "登录失败：用户名或密码错误！");
            return result;
        }
    }
}