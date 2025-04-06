package com.example.dao;

import com.example.modle.User;

public interface UserDao {
    // 根据用户名查找用户
    User findByUsername(String username);
    
    // 添加用户
    boolean add(User user);
    
    // 验证用户登录
    boolean validate(String username, String password);
} 