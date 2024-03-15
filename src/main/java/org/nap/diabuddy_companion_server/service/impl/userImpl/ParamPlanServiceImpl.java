package org.nap.diabuddy_companion_server.service.impl.userImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nap.diabuddy_companion_server.entity.user.User;
import org.nap.diabuddy_companion_server.mapper.user.ParamPlanMapper;
import org.nap.diabuddy_companion_server.service.user.ParamPlanService;
import org.springframework.stereotype.Service;

@Service
public class ParamPlanServiceImpl extends ServiceImpl<ParamPlanMapper, User> implements ParamPlanService {
}
