package com.example.foodchooser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("food")
public class Food {
    @TableId(type = IdType.AUTO)//数据库主键的生成策略是自动生长
    private Integer id;
    private String name;

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
}
