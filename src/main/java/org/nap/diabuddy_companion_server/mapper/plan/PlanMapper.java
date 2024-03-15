package org.nap.diabuddy_companion_server.mapper.plan;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.nap.diabuddy_companion_server.entity.plan.Plan;

@Mapper
public interface PlanMapper extends BaseMapper<Plan> {
}
