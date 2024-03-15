package org.nap.diabuddy_companion_server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.nap.diabuddy_companion_server.entity.Record;
import org.nap.diabuddy_companion_server.entity.RecordExercise;

public interface RecordExerciseService extends IService<RecordExercise> {
    RecordExercise findLastExercise(Integer userId);
}
