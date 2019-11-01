package com.ming.po;

import java.io.Serializable;
import java.util.Date;

/**
 * mybatis中的二级缓存没有指定一定要存在内存当中,所以需要类实现序列化接口
 */
public class User implements Serializable {

    // must be exists
    // * id
    // * default empty(non-param) construction
    private int userId;

    private String userName;

    private Date birthday;

    private String userSex;

    private String userAddr;

    public User(){

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserAddr() {
        return userAddr;
    }

    public void setUserAddr(String userAddr) {
        this.userAddr = userAddr;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", birthday=" + birthday +
                ", userSex='" + userSex + '\'' +
                ", userAddr='" + userAddr + '\'' +
                '}';
    }
}
