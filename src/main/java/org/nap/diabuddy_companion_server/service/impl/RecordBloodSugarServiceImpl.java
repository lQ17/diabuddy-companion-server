package org.nap.diabuddy_companion_server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nap.diabuddy_companion_server.entity.Record;
import org.nap.diabuddy_companion_server.entity.RecordBloodSugar;
import org.nap.diabuddy_companion_server.mapper.RecordBloodSugarMapper;
import org.nap.diabuddy_companion_server.mapper.RecordMapper;
import org.nap.diabuddy_companion_server.service.RecordBloodSugarService;
import org.nap.diabuddy_companion_server.service.RecordService;
import org.springframework.stereotype.Service;

@Service
public class RecordBloodSugarServiceImpl extends ServiceImpl<RecordBloodSugarMapper, RecordBloodSugar> implements RecordBloodSugarService {
}