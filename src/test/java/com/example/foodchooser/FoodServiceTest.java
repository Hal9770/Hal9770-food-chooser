package com.example.foodchooser;

import com.example.foodchooser.entity.Food;
import com.example.foodchooser.service.FoodService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// 确保是这个包，不要用 org.junit.Assert
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class FoodServiceTest {

    @Autowired
    private FoodService foodService;

    @Test
    public void testAddFood() {
        Food food = foodService.getRandomFood();
        assertNotNull(food, "获取到的食物不应该为空");
        System.out.println(food.getIdv2());
        System.out.println(food.getNamev2());
    }
}
