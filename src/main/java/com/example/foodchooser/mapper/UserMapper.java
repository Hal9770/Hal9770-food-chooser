package com.example.foodchooser.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.foodchooser.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
