package org.nap.diabuddy_companion_server.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JWTInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 获取请求头中的token
        String token = request.getHeader("Authorization");
        if (token != null) {
            try {
                // 使用 JWTUtils 验证 token
                JWTUtils.verify(token);
                return true;
            } catch (Exception e) {
                // token 验证失败，返回错误信息
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is invalid or expired");
                return false;
            }
        }
        // 没有找到token，返回错误信息
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is missing");
        return false;
    }
}
