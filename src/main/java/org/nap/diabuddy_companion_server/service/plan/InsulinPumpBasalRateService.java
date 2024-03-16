package org.nap.diabuddy_companion_server.service.plan;

import com.baomidou.mybatisplus.extension.service.IService;
import org.nap.diabuddy_companion_server.entity.plan.InsulinPumpBasalRate;

public interface InsulinPumpBasalRateService extends IService<InsulinPumpBasalRate> {
    InsulinPumpBasalRate findLastRate(Integer userId);
    InsulinPumpBasalRate findLatestByUserId(Integer userId);
}
