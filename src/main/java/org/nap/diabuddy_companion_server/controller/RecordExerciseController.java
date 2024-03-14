package org.nap.diabuddy_companion_server.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.nap.diabuddy_companion_server.common.R;
import org.nap.diabuddy_companion_server.entity.Record;
import org.nap.diabuddy_companion_server.entity.RecordDiet;
import org.nap.diabuddy_companion_server.entity.RecordExercise;
import org.nap.diabuddy_companion_server.service.RecordExerciseService;
import org.nap.diabuddy_companion_server.service.RecordService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/record")
public class RecordExerciseController {

    @Resource
    private RecordService recordService;

    @Resource
    private RecordExerciseService recordExerciseService;

    @PostMapping("/exercise")
    @Transactional
    public R<Object> addRecord(@RequestBody RecordExercise recordExercise){
        boolean isSaved = recordExerciseService.save(recordExercise);

        if (!isSaved) {
            return R.error("未成功记录");
        }

        // 构造一个Record
        Record record = new Record();
        record.setRecordId(recordExercise.getId());
        record.setRecordTime(recordExercise.getRecordTime());
        record.setRecordType("exercise");
        record.setUserId(recordExercise.getUserId());

        // 保存 Record 到数据库
        recordService.save(record);

        return R.success(null);

    }

}
