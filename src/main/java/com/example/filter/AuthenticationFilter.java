package com.example.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/employee/*"})
public class AuthenticationFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化过滤器
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // 获取当前请求的URI
        String requestURI = httpRequest.getRequestURI();
        
        // 检查是否是登录或注册页面
        if (requestURI.contains("/login") || requestURI.contains("/register")) {
            chain.doFilter(request, response);
            return;
        }
        
        // 检查用户是否已登录
        HttpSession session = httpRequest.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            // 用户已登录，继续请求
            chain.doFilter(request, response);
        } else {
            // 用户未登录，重定向到登录页面
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
        }
    }
    
    @Override
    public void destroy() {
        // 销毁过滤器
    }
} 