package com.example.foodchooser.controller;

import com.example.foodchooser.entity.Food;
import com.example.foodchooser.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RandomFoodController {

    @Autowired
    private FoodService foodService;

    @GetMapping("/random-food")
    public Food getRandomFood() {return foodService.getRandomFood();}

    // 【新增接口】按食堂筛选
    @GetMapping("/random-food/by-position")
    public Food getRandomFoodByPosition(@RequestParam("position") String position) {
        // 这里的 @RequestParam("position") 对应 URL 问号后面的 position 参数
        return foodService.getRandomFoodByPosition(position);
    }
}
