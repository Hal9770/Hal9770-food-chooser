package com.example.foodchooser;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain; // 新版核心类

@Configuration
@EnableWebSecurity//开启web安全功能
public class SecurityConfig {//配置类

    //1.密码加密器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. 安全过滤链配置（核心：配置“放行”规则）
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 开启 CSRF 保护（暂时不用管，后面讲 JWT 时会关闭）
                .csrf().disable()

                // 【核心配置开始】
                .authorizeRequests()
                // 设置“白名单”：这些路径，允许匿名访问
                .antMatchers(
                        "/users/login",      // 登录接口
                        "/users/register",   // 注册接口
                        "/",                // 首页
                        "/index.html",      // 首页页面
                        "/script.js",       // 前端脚本
                        "/random-food",     // 查食物接口
                        "/random-food/by-position" // 按位置查食物
                ).permitAll()

                // 除了上面那些，其他任何请求都需要认证
                .anyRequest().authenticated()
                // 【核心配置结束】

                // 暂时启用默认的表单登录（方便我们后续测试）
                .and()
                .formLogin();

        return http.build();
    }
}
