package org.nap.diabuddy_companion_server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nap.diabuddy_companion_server.entity.Record;
import org.nap.diabuddy_companion_server.entity.RecordAgent;
import org.nap.diabuddy_companion_server.entity.RecordExercise;
import org.nap.diabuddy_companion_server.mapper.RecordExerciseMapper;
import org.nap.diabuddy_companion_server.mapper.RecordMapper;
import org.nap.diabuddy_companion_server.service.RecordExerciseService;
import org.nap.diabuddy_companion_server.service.RecordService;
import org.springframework.stereotype.Service;

@Service
public class RecordExerciseServiceImpl extends ServiceImpl<RecordExerciseMapper, RecordExercise> implements RecordExerciseService {

    public RecordExercise findLastExercise(Integer userId) {
        QueryWrapper<RecordExercise> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId) // 筛选特定用户
                .orderByDesc("record_time") // 按记录时间降序
                .last("LIMIT 1"); // 限制结果为1条
        return this.getOne(queryWrapper);
    }
}
