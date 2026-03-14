package com.example.foodchooserv2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.foodchooserv2.entity.Food;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FoodMapper extends BaseMapper<Food> {

}
