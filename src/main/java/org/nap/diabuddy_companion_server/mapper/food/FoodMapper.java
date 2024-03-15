package org.nap.diabuddy_companion_server.mapper.food;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.nap.diabuddy_companion_server.entity.food.Food;

@Mapper
public interface FoodMapper extends BaseMapper<Food> {

}
