package com.example.foodchooser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.lang.reflect.Type;

@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer iduser;
    private String username;
    private String password;

    //空构造器
    public User() {}
    //全参构造器
    public User(Integer iduser, String username, String password) {
        this.iduser = iduser;
        this.username = username;
        this.password = password;
    }

    public Integer getIduser() {return iduser;}
    public void setIduser(Integer iduser) {this.iduser = iduser;}
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    //toString方法，方便调试和日志输出
    @Override
    public String toString() {
        return "User [id=" + iduser + ", username=" + username + ", password=" + password + "]";
    }
}
