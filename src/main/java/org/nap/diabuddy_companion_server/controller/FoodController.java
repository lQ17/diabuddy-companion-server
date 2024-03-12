package org.nap.diabuddy_companion_server.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.nap.diabuddy_companion_server.common.R;
import org.nap.diabuddy_companion_server.entity.Food;
import org.nap.diabuddy_companion_server.entity.FoodVO;
import org.nap.diabuddy_companion_server.entity.UserVO;
import org.nap.diabuddy_companion_server.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/food")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @GetMapping("/list")
    public R<Object> getFoodList(@RequestParam(value = "foodCategory", required = false) String foodCategory,
                               @RequestParam(value = "foodTinyCategory", required = false) String foodTinyCategory,
                               @RequestParam(value = "foodName", required = false) String foodName) {
        LambdaQueryWrapper<Food> queryWrapper = new LambdaQueryWrapper<>();

        if (foodName != null) {
            // 根据食物名查询
            queryWrapper.like(Food::getFoodName, foodName);
        } else if (foodCategory != null && foodTinyCategory != null) {
            // 根据食物分类和食物小分类查询
            queryWrapper.eq(Food::getFoodCategory, foodCategory)
                    .eq(Food::getFoodTinyCategory, foodTinyCategory);
        } else {
            // 参数不足
            return R.error("缺少食物信息，请重新查询");
        }


        List<FoodVO> list = foodService.list(queryWrapper).stream()
                .map(this::toVO) // 使用方法引用来应用转换
                .collect(Collectors.toList()); // 收集转换结果为一个新列表
        Map<String, Object> response = new HashMap<>();
        response.put("list", list);
        return R.success(response);
    }


    private FoodVO toVO(Food food){
        FoodVO foodVO = new FoodVO();
        foodVO.setFoodId(food.getFoodId());
        foodVO.setFoodName(food.getFoodName());
        foodVO.setFoodPic(food.getFoodPic());
        foodVO.setEnergy(food.getEnergy());
        foodVO.setCarb(food.getCarb());
        return foodVO;
    }
}
