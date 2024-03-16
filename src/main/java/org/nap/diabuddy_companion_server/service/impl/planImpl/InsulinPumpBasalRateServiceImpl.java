package org.nap.diabuddy_companion_server.service.impl.planImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.nap.diabuddy_companion_server.entity.plan.InsulinPumpBasalRate;
import org.nap.diabuddy_companion_server.mapper.plan.InsulinPumpBasalRateMapper;
import org.nap.diabuddy_companion_server.service.plan.InsulinPumpBasalRateService;
import org.springframework.stereotype.Service;

@Service
public class InsulinPumpBasalRateServiceImpl extends ServiceImpl<InsulinPumpBasalRateMapper, InsulinPumpBasalRate> implements InsulinPumpBasalRateService {

    // 首页用
    public InsulinPumpBasalRate findLastRate(Integer userId) {
        QueryWrapper<InsulinPumpBasalRate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .orderByDesc("start_time")
                .last("LIMIT 1");

        return this.getOne(queryWrapper);
    }

    @Resource
    private InsulinPumpBasalRateMapper insulinPumpBasalRateMapper;

//    // 使用构造器注入 --
//    public InsulinPumpBasalRateServiceImpl(InsulinPumpBasalRateMapper insulinPumpBasalRateMapper) {
//        this.insulinPumpBasalRateMapper = insulinPumpBasalRateMapper;
//    }

    @Override
    // 手动设置页
    public InsulinPumpBasalRate findLatestByUserId(Integer userId) {
        return insulinPumpBasalRateMapper.findLatestByUserId(userId);
    }
}
