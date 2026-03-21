package com.example.foodchooser.controller;

import com.example.foodchooser.entity.User;
import com.example.foodchooser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public String login(@RequestBody User user) {
        User loginUser = userService.login(user.getUsername(), user.getPassword());
        if (loginUser != null) {
            return "登录成功！欢迎 " + loginUser.getUsername();
        } else {
            return "登录失败：用户名或密码错误！";
        }
    }
}
