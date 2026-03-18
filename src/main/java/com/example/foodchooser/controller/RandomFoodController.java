package com.example.foodchooser.controller;

import com.example.foodchooser.entity.Food;
import com.example.foodchooser.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RandomFoodController {

    @Autowired
    private FoodService foodService;

    @GetMapping("/random-food")
    public Food getRandomFood() {return foodService.getRandomFood();}
}
