package org.nap.diabuddy_companion_server.service.impl.planImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nap.diabuddy_companion_server.entity.user.User;
import org.nap.diabuddy_companion_server.mapper.plan.UserPlanMapper;
import org.nap.diabuddy_companion_server.service.plan.UserPlanService;
import org.springframework.stereotype.Service;

@Service
public class UserPlanServiceImpl extends ServiceImpl<UserPlanMapper, User> implements UserPlanService {
}
