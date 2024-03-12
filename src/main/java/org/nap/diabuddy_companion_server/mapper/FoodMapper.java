package org.nap.diabuddy_companion_server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.nap.diabuddy_companion_server.entity.Food;

@Mapper
public interface FoodMapper extends BaseMapper<Food> {
}
