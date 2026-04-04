package com.example.foodchooser.filter;

import com.example.foodchooser.utils.JwtUtils; // 引入你的工具类
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 从请求头拿到 Token
        String token = request.getHeader("Authorization");

        // 2. 如果有 Token，就去验证它
        if (token != null && !token.isEmpty()) {
            String username = JwtUtils.validateToken(token); // 调用你的工具类

            // 3. 如果 Token 合法（解析出了用户名）
            if (username != null) {
                // 【魔法代码】手动把用户标记为“已登录”状态，这样 Security 就不会拦截他了
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 4. 放行，交给下一个过滤器（也就是你上面配置的 SecurityFilterChain 去判断白名单）
        filterChain.doFilter(request, response);
    }
}

