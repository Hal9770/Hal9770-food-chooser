package com.example.foodchooser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("foodv2")
public class Food {
    @TableId(type = IdType.AUTO)//数据库主键的生成策略是自动生长
    private Integer idfoodv2;
    private String namev2;
    private String positionv2;

    public Integer getIdv2() {return idfoodv2;}
    public void setIdv2(Integer idfoodv2) {this.idfoodv2 = idfoodv2;}
    public String getNamev2() {return namev2;}
    public void setNamev2(String namev2) {this.namev2 = namev2;}
    public String getPositionv2() {return positionv2;}
    public void setPositionv2(String positionv2) {this.positionv2 = positionv2;}
}
