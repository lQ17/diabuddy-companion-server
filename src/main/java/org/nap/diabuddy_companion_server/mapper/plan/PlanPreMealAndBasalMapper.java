package org.nap.diabuddy_companion_server.mapper.plan;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.nap.diabuddy_companion_server.entity.plan.Plan;
import org.nap.diabuddy_companion_server.entity.plan.PlanPreMealAndBasal;

@Mapper
public interface PlanPreMealAndBasalMapper extends BaseMapper<PlanPreMealAndBasal> {
}
