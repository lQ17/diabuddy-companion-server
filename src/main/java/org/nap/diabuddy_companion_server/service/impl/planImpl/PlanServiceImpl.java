package org.nap.diabuddy_companion_server.service.impl.planImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nap.diabuddy_companion_server.entity.plan.Plan;
import org.nap.diabuddy_companion_server.mapper.plan.PlanMapper;
import org.nap.diabuddy_companion_server.service.plan.PlanService;
import org.springframework.stereotype.Service;

@Service
public class PlanServiceImpl extends ServiceImpl<PlanMapper, Plan> implements PlanService {
}
