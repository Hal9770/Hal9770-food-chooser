package com.example.foodchooser.service;

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
}
