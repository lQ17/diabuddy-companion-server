package org.nap.diabuddy_companion_server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nap.diabuddy_companion_server.entity.Record;
import org.nap.diabuddy_companion_server.entity.RecordAgent;
import org.nap.diabuddy_companion_server.mapper.RecordAgentMapper;
import org.nap.diabuddy_companion_server.mapper.RecordMapper;
import org.nap.diabuddy_companion_server.service.RecordAgentService;
import org.nap.diabuddy_companion_server.service.RecordService;
import org.springframework.stereotype.Service;

@Service
public class RecordAgentServiceImpl extends ServiceImpl<RecordAgentMapper, RecordAgent> implements RecordAgentService {
}
