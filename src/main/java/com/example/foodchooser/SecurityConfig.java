package com.example.foodchooser;

import com.example.foodchooser.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain; // 新版核心类
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        http
                // 【新增】把 JWT 过滤器加在用户名密码校验之前
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                // 开启 CSRF 保护（暂时不用管，后面讲 JWT 时会关闭）
                //.csrf().disable()
                // 【修改1】关闭 CSRF。因为前后端分离+JWT模式，不需要CSRF防护了
                .csrf(csrf -> csrf.disable())
                // 【修改2】关闭默认的 Session 机制，因为我们现在要“无状态”了
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))


                // 【核心配置开始】
                .authorizeRequests()
                // 设置“白名单”：这些路径，允许匿名访问
                .antMatchers(

                        "/users/captcha",
                        "/users/login",      // 登录接口
                        "/users/register",   // 注册接口
                        "/",                // 首页
                        "/index.html",      // 首页页面
                        "/script.js"       // 前端脚本
                        //"/random-food",     // 查食物接口
                        //"/random-food/by-position" // 按位置查食物
                ).permitAll()

                // 除了上面那些，其他任何请求都需要认证
                .anyRequest().authenticated()
                // 【核心配置结束】

                // 【修改3】非常重要！把原来的 .and().formLogin() 删掉！换成下面这句：
                // 意思是：如果没有登录（没有合法JWT），不要跳转页面，直接返回 401 错误状态码
                .and()
                .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) ->
                {
                    response.setStatus(401);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":401, \"message\":\"未登录或Token已过期\"}");
                }));

        return http.build();
    }
}
