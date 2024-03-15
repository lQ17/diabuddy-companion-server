package org.nap.diabuddy_companion_server.service.impl.planImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nap.diabuddy_companion_server.entity.plan.PlanAgent;
import org.nap.diabuddy_companion_server.entity.plan.PlanPreMealAndBasal;
import org.nap.diabuddy_companion_server.mapper.plan.PlanAgentMapper;
import org.nap.diabuddy_companion_server.mapper.plan.PlanPreMealAndBasalMapper;
import org.nap.diabuddy_companion_server.service.plan.PlanAgentService;
import org.nap.diabuddy_companion_server.service.plan.PlanPreMealAndBasalService;
import org.springframework.stereotype.Service;

@Service
public class PlanPreMealAndBasalServiceImpl extends ServiceImpl<PlanPreMealAndBasalMapper, PlanPreMealAndBasal> implements PlanPreMealAndBasalService {
}
