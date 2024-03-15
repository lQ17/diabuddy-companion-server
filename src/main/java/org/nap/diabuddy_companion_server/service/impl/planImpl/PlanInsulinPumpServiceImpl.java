package org.nap.diabuddy_companion_server.service.impl.planImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nap.diabuddy_companion_server.entity.plan.PlanAgent;
import org.nap.diabuddy_companion_server.entity.plan.PlanInsulinPump;
import org.nap.diabuddy_companion_server.mapper.plan.PlanAgentMapper;
import org.nap.diabuddy_companion_server.mapper.plan.PlanInsulinPumpMapper;
import org.nap.diabuddy_companion_server.service.plan.PlanAgentService;
import org.nap.diabuddy_companion_server.service.plan.PlanInsulinPumpService;
import org.springframework.stereotype.Service;

@Service
public class PlanInsulinPumpServiceImpl extends ServiceImpl<PlanInsulinPumpMapper, PlanInsulinPump> implements PlanInsulinPumpService {
}
