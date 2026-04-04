package com.example.foodchooser.controller;

import com.example.foodchooser.entity.User;
import com.example.foodchooser.service.UserService;
import com.example.foodchooser.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users") // 给所有接口加一个统一前缀，比如 /users
public class UserController {

    @Autowired
    private UserService userService;

    // 注册接口
    // 访问方式：POST http://localhost:8080/users/register
    // 前端需要在请求体里发送 JSON 数据，例如：{"username": "zhangsan", "password": "123456"}
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        boolean success = userService.register(user.getUsername(), user.getPassword());
        if (success) {
            return "注册成功！";
        } else {
            return "注册失败：用户名已存在！";
        }
    }

    // 登录接口
    // 访问方式：POST http://localhost:8080/users/login
    // 前端同样发送 JSON 数据
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>(); // 用来装最终返回给前端的JSON数据
        User loginUser = userService.login(user.getUsername(), user.getPassword());
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