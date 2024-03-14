package org.nap.diabuddy_companion_server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nap.diabuddy_companion_server.entity.Record;
import org.nap.diabuddy_companion_server.entity.RecordInjection;
import org.nap.diabuddy_companion_server.mapper.RecordInjectionMapper;
import org.nap.diabuddy_companion_server.mapper.RecordMapper;
import org.nap.diabuddy_companion_server.service.RecordInjectionService;
import org.nap.diabuddy_companion_server.service.RecordService;
import org.springframework.stereotype.Service;

@Service
public class RecordInjectionServiceImpl extends ServiceImpl<RecordInjectionMapper, RecordInjection> implements RecordInjectionService {
}
