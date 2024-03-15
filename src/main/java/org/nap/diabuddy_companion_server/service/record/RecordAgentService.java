package org.nap.diabuddy_companion_server.service.record;

import com.baomidou.mybatisplus.extension.service.IService;
import org.nap.diabuddy_companion_server.entity.record.RecordAgent;

public interface RecordAgentService extends IService<RecordAgent> {
    RecordAgent findLastAgent(Integer userId);
}
