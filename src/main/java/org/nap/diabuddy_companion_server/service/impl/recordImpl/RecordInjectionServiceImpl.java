package org.nap.diabuddy_companion_server.service.impl.recordImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nap.diabuddy_companion_server.entity.record.RecordInjection;
import org.nap.diabuddy_companion_server.mapper.record.RecordInjectionMapper;
import org.nap.diabuddy_companion_server.service.record.RecordInjectionService;
import org.springframework.stereotype.Service;

@Service
public class RecordInjectionServiceImpl extends ServiceImpl<RecordInjectionMapper, RecordInjection> implements RecordInjectionService {
}
