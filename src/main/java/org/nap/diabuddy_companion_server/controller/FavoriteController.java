package org.nap.diabuddy_companion_server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.nap.diabuddy_companion_server.common.R;
import org.nap.diabuddy_companion_server.entity.Favorite;
import org.nap.diabuddy_companion_server.entity.Food;
import org.nap.diabuddy_companion_server.entity.VO.FoodVOForList;
import org.nap.diabuddy_companion_server.service.FavoriteService;
import org.nap.diabuddy_companion_server.service.FoodService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/food")
public class FavoriteController {

    @Resource
    private FavoriteService favoriteService;

    @Resource
    private FoodService foodService;

    @GetMapping("/favorite/{userId}/{foodId}")
    public R<Object> isFavorite(@PathVariable int userId, @PathVariable int foodId) {
        LambdaQueryWrapper<Favorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Favorite::getUserId,userId).eq(Favorite::getFoodId,foodId);
        Favorite favorite = favoriteService.getOne(queryWrapper);

        Map<String,Object> res = new HashMap<>();

        boolean isFavorite;

        isFavorite = favorite != null;

        res.put("isFavorite",isFavorite);

        return R.success(res);
    }

    @PostMapping("/favorite")
    public R<Object> addFavorite(@RequestBody Favorite favorite){
        System.out.println(favorite);
        if(favorite.getFoodId() == null || favorite.getUserId() == null){
            return R.error("参数错误，请重试");
        }

        favoriteService.save(favorite);

        return R.success(null);
    }

    @DeleteMapping("/favorite/{userId}/{foodId}")
    public R<Object> delFavorite(@PathVariable int userId, @PathVariable int foodId){
        LambdaQueryWrapper<Favorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Favorite::getUserId,userId).eq(Favorite::getFoodId,foodId);
        boolean isRemove = favoriteService.remove(queryWrapper);
        if(!isRemove) {
            return R.error("您并未收藏该食物");
        }
        return R.success(null);
    }

    @GetMapping("/favorite-list/{userId}")
    public R<List<FoodVOForList>> getFavoriteList(@PathVariable int userId){
        LambdaQueryWrapper<Favorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Favorite::getUserId,userId);

        // 查询收藏表，获取该用户收藏的所有食物ID
        List<Favorite> favorites = favoriteService.list(queryWrapper);

        if (favorites.isEmpty()) {
            // 用户没有收藏任何食物
            return R.success(Collections.emptyList());
        }

        // 从收藏中提取食物ID
        List<Integer> foodIds = favorites.stream()
                .map(Favorite::getFoodId)
                .distinct()
                .collect(Collectors.toList());

        // 根据食物ID查询食物详情
        List<Food> foods = foodService.listByIds(foodIds);
        // 将Food实体转换为FoodVOForList
        List<FoodVOForList> foodVOForLists = foods.stream()
                .map(this::convertToFoodVOForList)
                .collect(Collectors.toList());

        return R.success(foodVOForLists);
    }

    private FoodVOForList convertToFoodVOForList(Food food) {
        // 这里应根据您的需求来转换Food实体到FoodVOForList
        FoodVOForList vo = new FoodVOForList();
        vo.setFoodId(food.getFoodId());
        vo.setFoodName(food.getFoodName());
        vo.setFoodPic(food.getFoodPic());
        vo.setEnergy(food.getEnergy());
        vo.setCarb(food.getCarb());

        // 设置其他需要从Food转换到FoodVOForList的属性
        return vo;
    }
}
