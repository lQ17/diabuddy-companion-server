package org.nap.diabuddy_companion_server.service.impl.planImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nap.diabuddy_companion_server.entity.plan.PlanAgent;
import org.nap.diabuddy_companion_server.entity.plan.PlanPremixed;
import org.nap.diabuddy_companion_server.mapper.plan.PlanAgentMapper;
import org.nap.diabuddy_companion_server.mapper.plan.PlanPremixedMapper;
import org.nap.diabuddy_companion_server.service.plan.PlanAgentService;
import org.nap.diabuddy_companion_server.service.plan.PlanPremixedService;
import org.springframework.stereotype.Service;

@Service
public class PlanPremixedServiceImpl extends ServiceImpl<PlanPremixedMapper, PlanPremixed> implements PlanPremixedService {
}
