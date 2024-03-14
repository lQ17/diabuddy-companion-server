package org.nap.diabuddy_companion_server.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.nap.diabuddy_companion_server.common.R;
import org.nap.diabuddy_companion_server.entity.Food;
import org.nap.diabuddy_companion_server.entity.VO.FoodVOForDetail;
import org.nap.diabuddy_companion_server.entity.VO.FoodVOForList;
import org.nap.diabuddy_companion_server.entity.VO.FoodVOForTotalDetail;
import org.nap.diabuddy_companion_server.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


        List<FoodVOForList> list = foodService.list(queryWrapper).stream()
                .map(this::toVOForList) // 使用方法引用来应用转换
                .collect(Collectors.toList()); // 收集转换结果为一个新列表
        Map<String, Object> response = new HashMap<>();
        response.put("list", list);
        return R.success(response);
    }

    @GetMapping("/detail/{id}")
    public R<Object> getFoodDetail(@PathVariable(value = "id") Integer foodId) {
        if (foodId == null) {
            // 参数不足或错误
            return R.error("缺少食物信息，请重新查询");
        }

        Food food = foodService.getById(foodId);
        if (food == null) {
            // 未找到对应的食物信息
            return R.error("未找到对应的食物信息");
        }

        FoodVOForDetail foodVOForDetail = toVOForDetail(food);
        return R.success(foodVOForDetail);
    }

    @GetMapping("/total-detail/{id}")
    public R<Object> getFoodTotalDetail(@PathVariable(value = "id") Integer foodId){
        if (foodId == null) {
            // 参数不足或错误
            return R.error("缺少食物信息，请重新查询");
        }
        Food food = foodService.getById(foodId);
        if (food == null) {
            // 未找到对应的食物信息
            return R.error("未找到对应的食物信息");
        }
        FoodVOForTotalDetail foodVOForTotalDetail = toVOForTotalDetail(food);
        return R.success(foodVOForTotalDetail);
    }

    @PostMapping("/add")
    public R<Object> updateFoodFromUser(@RequestBody Food food){

        // 无意义食物
        if(food.getFoodName() == null || food.getCarb() == null){
            return R.error("无效食物，至少填写食物名称和碳水化合物质量");
        }

        // 如果上传公共食物，先把isReviewed置0
        if(food.getIsPublicFood() == 1){
            food.setIsReviewed(0);
        }


        boolean isSaved = foodService.save(food);

        if(!isSaved){
            return R.error("提交了错误的信息，请重试");
        }
        return R.success(null);
    }

    private FoodVOForList toVOForList(Food food){
        FoodVOForList foodVOForList = new FoodVOForList();
        foodVOForList.setFoodId(food.getFoodId());
        foodVOForList.setFoodName(food.getFoodName());
        foodVOForList.setFoodPic(food.getFoodPic());
        foodVOForList.setEnergy(food.getEnergy());
        foodVOForList.setCarb(food.getCarb());
        return foodVOForList;
    }

    private FoodVOForDetail toVOForDetail(Food food){
        FoodVOForDetail foodVOForDetail = new FoodVOForDetail();
        foodVOForDetail.setFoodId(food.getFoodId());
        foodVOForDetail.setFoodName(food.getFoodName());
        foodVOForDetail.setFoodPic(food.getFoodPic());
        foodVOForDetail.setEnergy(food.getEnergy());
        foodVOForDetail.setCarb(food.getCarb());
        foodVOForDetail.setFat(food.getFat());
        foodVOForDetail.setProtein(food.getProtein());
        foodVOForDetail.setGlycemicIndex(food.getGlycemicIndex());
        foodVOForDetail.setGlycemicLoad(food.getGlycemicLoad());
        foodVOForDetail.setDietaryFiber(food.getDietaryFiber());
        foodVOForDetail.setSodium(food.getSodium());
        return foodVOForDetail;
    }

    private FoodVOForTotalDetail toVOForTotalDetail(Food food) {
        FoodVOForTotalDetail foodVOForTotalDetail = new FoodVOForTotalDetail();
        foodVOForTotalDetail.setFoodName(food.getFoodName());
        foodVOForTotalDetail.setEdiblePart(food.getEdiblePart());
        foodVOForTotalDetail.setWater(food.getWater());
        foodVOForTotalDetail.setEnergy(food.getEnergy());
        foodVOForTotalDetail.setProtein(food.getProtein());
        foodVOForTotalDetail.setFat(food.getFat());
        foodVOForTotalDetail.setCholesterol(food.getCholesterol());
        foodVOForTotalDetail.setAsh(food.getAsh());
        foodVOForTotalDetail.setCarb(food.getCarb());
        foodVOForTotalDetail.setDietaryFiber(food.getDietaryFiber());
        foodVOForTotalDetail.setCarotene(food.getCarotene());
        foodVOForTotalDetail.setVitaminA(food.getVitaminA());
        foodVOForTotalDetail.setAlphaTe(food.getAlphaTe());
        foodVOForTotalDetail.setThiamin(food.getThiamin());
        foodVOForTotalDetail.setRiboflavin(food.getRiboflavin());
        foodVOForTotalDetail.setNiacin(food.getNiacin());
        foodVOForTotalDetail.setVitaminC(food.getVitaminC());
        foodVOForTotalDetail.setCalcium(food.getCalcium());
        foodVOForTotalDetail.setPhosphorus(food.getPhosphorus());
        foodVOForTotalDetail.setPotassium(food.getPotassium());
        foodVOForTotalDetail.setSodium(food.getSodium());
        foodVOForTotalDetail.setMagnesium(food.getMagnesium());
        foodVOForTotalDetail.setIron(food.getIron());
        foodVOForTotalDetail.setZinc(food.getZinc());
        foodVOForTotalDetail.setSelenium(food.getSelenium());
        foodVOForTotalDetail.setCopper(food.getCopper());
        foodVOForTotalDetail.setManganese(food.getManganese());
        foodVOForTotalDetail.setIodine(food.getIodine());
        foodVOForTotalDetail.setSaturatedFattyAcids(food.getSaturatedFattyAcids());
        foodVOForTotalDetail.setMonounsaturatedFattyAcids(food.getMonounsaturatedFattyAcids());
        foodVOForTotalDetail.setPolyunsaturatedFattyAcids(food.getPolyunsaturatedFattyAcids());
        foodVOForTotalDetail.setTotalFattyAcids(food.getTotalFattyAcids());

        return foodVOForTotalDetail;
    }

}
