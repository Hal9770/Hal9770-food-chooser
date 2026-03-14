package com.example.foodchooser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("foodv2")
public class Food {
    @TableId(type = IdType.AUTO)//数据库主键的生成策略是自动生长
    private Integer idfoodv2;
    private String namev2;

    public Integer getId() {return idfoodv2;}
    public void setId(Integer idfoodv2) {this.idfoodv2 = idfoodv2;}
    public String getName() {return namev2;}
    public void setName(String namev2) {this.namev2 = namev2;}
}
