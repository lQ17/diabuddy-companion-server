package org.nap.diabuddy_companion_server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nap.diabuddy_companion_server.entity.Record;
import org.nap.diabuddy_companion_server.entity.RecordExercise;
import org.nap.diabuddy_companion_server.mapper.RecordExerciseMapper;
import org.nap.diabuddy_companion_server.mapper.RecordMapper;
import org.nap.diabuddy_companion_server.service.RecordExerciseService;
import org.nap.diabuddy_companion_server.service.RecordService;
import org.springframework.stereotype.Service;

@Service
public class RecordExerciseServiceImpl extends ServiceImpl<RecordExerciseMapper, RecordExercise> implements RecordExerciseService {
}
