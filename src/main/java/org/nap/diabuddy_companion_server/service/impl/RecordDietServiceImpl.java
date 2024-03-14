package org.nap.diabuddy_companion_server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nap.diabuddy_companion_server.entity.Record;
import org.nap.diabuddy_companion_server.entity.RecordDiet;
import org.nap.diabuddy_companion_server.mapper.RecordDietMapper;
import org.nap.diabuddy_companion_server.mapper.RecordMapper;
import org.nap.diabuddy_companion_server.service.RecordDietService;
import org.nap.diabuddy_companion_server.service.RecordService;
import org.springframework.stereotype.Service;

@Service
public class RecordDietServiceImpl extends ServiceImpl<RecordDietMapper, RecordDiet> implements RecordDietService {
}
