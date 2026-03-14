package com.example.foodchooserv2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("food")
public class Food {
    @TableId(type = IdType.AUTO)//数据库主键的生成策略是自动生长
    private int idfood_v2;
    private String name_v2;

    public int getIdfood_v2() {return idfood_v2;}
    public void setIdfood_v2(int idfood_v2) {this.idfood_v2 = idfood_v2;}
    public String getName_v2() {return name_v2;}
    public void setName_v2(String name_v2) {this.name_v2 = name_v2;}
}
