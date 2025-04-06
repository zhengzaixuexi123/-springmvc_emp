package com.example.servlet;

import com.example.dao.UserDao;
import com.example.dao.UserDaoImpl;
import com.example.modle.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserDao userDao = new UserDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 显示注册页面
        request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 处理注册请求
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // 验证密码是否匹配
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "两次输入的密码不匹配");
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
            return;
        }
        
        // 检查用户名是否已存在
        if (userDao.findByUsername(username) != null) {
            request.setAttribute("error", "用户名已存在");
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
            return;
        }
        
        // 创建新用户
        User user = new User(username, password);
        if (userDao.add(user)) {
            // 注册成功，重定向到登录页面
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            // 注册失败，返回注册页面并显示错误信息
            request.setAttribute("error", "注册失败，请重试");
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
        }
    }
} 