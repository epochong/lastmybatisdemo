package com.ming.mapper;

import com.ming.po.User;

public interface UserMapper {

    //4 rules
    public User queryUserById(int myId) throws Exception;

    public void modifyUserById(User user) throws Exception;
}