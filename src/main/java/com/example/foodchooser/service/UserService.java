package com.example.foodchooser.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.foodchooser.entity.User;
import com.example.foodchooser.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * 用户注册
     * @param username 用户名
     * @param rawPassword 明文密码
     * @return 注册成功返回 true，失败返回 false
     */
    /**
     * 用户注册
     * @Transactional 注解确保方法执行成功后事务提交
     */
    @Transactional // 添加这个注解
    public boolean register(String username, String rawPassword) {
        // 1. 检查用户名是否已存在
        // 使用 QueryWrapper 构建查询条件：WHERE username = ?
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);

        User existingUser = userMapper.selectOne(queryWrapper);

        if (existingUser != null) {
            // 用户名已存在，注册失败
            return false;
        }

        // 2. 用户名可用，进行密码加密
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // 建新用户对象
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(encodedPassword);

        // 3. 存入数据库
        int rows = userMapper.insert(newUser);

        return rows > 0; // 影响行数 > 0 表示插入成功
    }

    /**
     * 用户登录验证
     * @param username 用户名
     *  specificParam 明文密码
     * @return 登录成功返回 User 对象，失败返回 null
     */
    public User login(String username, String rawPassword) {
        // 1. 根据用户名从数据库查出用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);

        User user = userMapper.selectOne(queryWrapper);

        // 2. 如果用户不存在，直接返回 null
        if (user == null) {
            return null;
        }

        // 3. 用户存在，进行密码匹配
        // matches(明文密码, 数据库密文)
        boolean isMatch = passwordEncoder.matches(rawPassword, user.getPassword());

        if (isMatch) {
            return user; // 密码正确，返回用户对象
        } else {
            return null; // 密码错误，返回 null
        }
    }
}

