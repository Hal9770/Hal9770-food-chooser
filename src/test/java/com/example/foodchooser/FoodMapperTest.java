package com.example.foodchooser;

import com.example.foodchooser.entity.Food;
import com.example.foodchooser.mapper.FoodMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest//组合注解，标注这是一个SpringBoot测试
public class FoodMapperTest {

    @Autowired//在测试中注入Bean
    private FoodMapper foodMapper;

    @Test
    public void testQuery() {
        List<Food> foods = foodMapper.selectList(null);//查询所有
        assertNotNull(foods);
        System.out.println("查询到 " + foods.size() + " 条数据");
        foods.forEach(f -> System.out.println(f.getId() + " - " + f.getName()));
    }
}