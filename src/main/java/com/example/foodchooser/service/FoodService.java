package com.example.foodchooser.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.foodchooser.entity.Food;
import com.example.foodchooser.mapper.FoodMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class FoodService {

    @Autowired//注入FoodMapper
    private FoodMapper foodMapper;

    public Food getRandomFood(){
        List<Food> allFoods = foodMapper.selectList(null);//Select from food
        if(allFoods.isEmpty()){
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(allFoods.size());
        return allFoods.get(index);
    }

    // 【新增方法】根据食堂位置随机获取
    public Food getRandomFoodByPosition(String position) {
        // 1. 创建一个条件构造器
        QueryWrapper<Food> queryWrapper = new QueryWrapper<>();

        // 2. 添加查询条件
        // 这行代码的意思是：WHERE positionv2 = '传入的position值'
        // eq 是 equals 的缩写
        queryWrapper.likeRight("positionv2", position);

        // 3. 执行查询
        // selectList(queryWrapper) 会自动生成 SQL:
        // SELECT * FROM foodv2 WHERE positionv2 = ?
        List<Food> foods = foodMapper.selectList(queryWrapper);

        // 4. 随机抽取逻辑（和之前一样）
        if(foods.isEmpty()){
            return null; // 如果该食堂没有数据，返回null
        }
        Random random = new Random();
        return foods.get(random.nextInt(foods.size()));
    }
}
