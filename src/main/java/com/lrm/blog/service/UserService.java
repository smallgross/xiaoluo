package com.lrm.blog.service;

import com.lrm.blog.po.User;

public interface UserService {
    public User  checkUser(String username,String password);
}
