package com.example.foodchooser;

import com.example.foodchooser.entity.Food;
import com.example.foodchooser.mapper.FoodMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class Application implements CommandLineRunner {  // 1. 实现 CommandLineRunner

    @Autowired
    private FoodMapper foodMapper;  // 2. 注入 Mapper

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<Food> foods = foodMapper.selectList(null);
        System.out.println("========== 测试查询结果 ==========");
        System.out.println("总条数：" + foods.size());
        for (int i = 0; i < foods.size(); i++) {
            Food foodv2 = foods.get(i);
            if (foodv2 == null) {
                System.out.println("第 " + i + " 个元素是 null");
            } else {
                System.out.println(foodv2.getId() + " - " + foodv2.getName());
            }
        }
        System.out.println("=================================");
    }
}