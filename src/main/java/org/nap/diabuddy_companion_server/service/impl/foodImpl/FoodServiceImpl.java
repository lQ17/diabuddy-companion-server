package org.nap.diabuddy_companion_server.service.impl.foodImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nap.diabuddy_companion_server.entity.food.Food;
import org.nap.diabuddy_companion_server.mapper.food.FoodMapper;
import org.nap.diabuddy_companion_server.service.food.FoodService;
import org.springframework.stereotype.Service;

@Service
public class FoodServiceImpl extends ServiceImpl<FoodMapper, Food> implements FoodService {

}
