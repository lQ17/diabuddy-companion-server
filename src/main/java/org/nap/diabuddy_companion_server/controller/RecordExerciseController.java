package org.nap.diabuddy_companion_server.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.nap.diabuddy_companion_server.common.R;
import org.nap.diabuddy_companion_server.entity.Record;
import org.nap.diabuddy_companion_server.entity.RecordAgent;
import org.nap.diabuddy_companion_server.entity.RecordDiet;
import org.nap.diabuddy_companion_server.entity.RecordExercise;
import org.nap.diabuddy_companion_server.service.RecordExerciseService;
import org.nap.diabuddy_companion_server.service.RecordService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    @PutMapping("/exercise")
    @Transactional
    public R<Object> updateRecord(@RequestBody RecordExercise recordExercise){

        if (recordExercise == null || recordExercise.getId() == null) {
            return R.error("请求数据无效");
        }

        boolean isUpdated = recordExerciseService.updateById(recordExercise);

        if(isUpdated){
            LambdaQueryWrapper<Record> queryWrapper = new LambdaQueryWrapper<>();
            // 根据 子表id 和 子表类型 确定总表的唯一数据
            queryWrapper.eq(Record::getRecordId,recordExercise.getId());
            queryWrapper.eq(Record::getRecordType,"exercise");
            // 获得总表实体
            Record record = recordService.getOne(queryWrapper);
            // 改时间
            record.setRecordTime(recordExercise.getRecordTime());
            // 存总表
            recordService.updateById(record);
        }else{
            return R.error("修改失败，请重试");
        }

        return R.success(null);
    }

    @GetMapping("/user-last-exercise-type/{userId}")
    public R<Object> getLastTypeAndTime(@PathVariable("userId") Integer userId){
        Map<String,Object> res = new HashMap<>();
        // 调用服务层方法获取用户最后一条用药记录
        RecordExercise lastRecordExercise = recordExerciseService.findLastExercise(userId);

        if (lastRecordExercise != null) {
            res.put("last_type",lastRecordExercise.getExerciseType());
            res.put("last_duration",lastRecordExercise.getDuration());
            return R.success(res);
        }else {
            return R.success(null);
        }
    }
}
