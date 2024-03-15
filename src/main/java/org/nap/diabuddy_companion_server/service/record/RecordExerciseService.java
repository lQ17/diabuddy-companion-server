package org.nap.diabuddy_companion_server.service.record;

import com.baomidou.mybatisplus.extension.service.IService;
import org.nap.diabuddy_companion_server.entity.record.RecordExercise;

public interface RecordExerciseService extends IService<RecordExercise> {
    RecordExercise findLastExercise(Integer userId);
}
