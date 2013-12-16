package com.example.jfinal_blog.dao;

import com.example.jfinal_blog.client.BlogMessage;
import com.example.jfinal_blog.client.User;

public interface DataHandler {
    BlogMessage onLogin(User user);
    BlogMessage onLogout(User user);
}
