package org.nap.diabuddy_companion_server.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.nap.diabuddy_companion_server.common.R;
import org.nap.diabuddy_companion_server.entity.Record;
import org.nap.diabuddy_companion_server.entity.RecordExercise;
import org.nap.diabuddy_companion_server.entity.RecordInjection;
import org.nap.diabuddy_companion_server.service.RecordInjectionService;
import org.nap.diabuddy_companion_server.service.RecordService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/record")
public class RecordInjectionController {

    @Resource
    private RecordService recordService;

    @Resource
    private RecordInjectionService recordInjectionService;

    @PostMapping("/injection")
    @Transactional
    public R<Object> addRecord(@RequestBody RecordInjection recordInjection){
        boolean isSaved = recordInjectionService.save(recordInjection);

        if (!isSaved) {
            return R.error("未成功记录");
        }

        // 构造一个Record
        Record record = new Record();
        record.setRecordId(recordInjection.getId());
        record.setRecordTime(recordInjection.getRecordTime());
        record.setRecordType("injection");
        record.setUserId(recordInjection.getUserId());

        // 保存 Record 到数据库
        recordService.save(record);

        return R.success(null);

    }

    @PutMapping("/injection")
    @Transactional
    public R<Object> updateRecord(@RequestBody RecordInjection recordInjection){

        // 构建UpdateWrapper
        UpdateWrapper<RecordInjection> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", recordInjection.getId());

        if (recordInjection.getId() == null) {
            return R.error("请求数据无效");
        }

        switch (recordInjection.getInjectionType()) {
            case "大剂量":
                recordInjection.setSquareWaveRate(null);
                recordInjection.setSquareWaveTime(null);
                updateWrapper.set("square_wave_rate", null);
                updateWrapper.set("square_wave_time", null);
                if (recordInjection.getBolus() == null) {
                    return R.error("缺少重要数值");
                }
                break;
            case "方波":
                recordInjection.setBolus(null);
                updateWrapper.set("bolus", null);
                if (recordInjection.getSquareWaveRate() == null || recordInjection.getSquareWaveTime() == null) {
                    return R.error("缺少重要数值");
                }
                break;
            case "双波":
                if (recordInjection.getSquareWaveRate() == null || recordInjection.getSquareWaveTime() == null || recordInjection.getBolus() == null) {
                    return R.error("缺少重要数值");
                }
                break;
            default:
                if (recordInjection.getSquareWaveRate() == null && recordInjection.getSquareWaveTime() == null && recordInjection.getBolus() == null) {
                    return R.error("缺少重要数值");
                }
                break;
        }



        // 使用updateWrapper执行更新，确保null值被更新
        boolean isUpdated = recordInjectionService.update(recordInjection, updateWrapper);

        if(isUpdated){
            LambdaQueryWrapper<Record> queryWrapper = new LambdaQueryWrapper<>();
            // 根据 子表id 和 子表类型 确定总表的唯一数据
            queryWrapper.eq(Record::getRecordId,recordInjection.getId());
            queryWrapper.eq(Record::getRecordType,"injection");
            // 获得总表实体
            Record record = recordService.getOne(queryWrapper);
            // 改时间
            record.setRecordTime(recordInjection.getRecordTime());
            // 存总表
            recordService.updateById(record);
        }else{
            return R.error("修改失败，请重试");
        }

        return R.success(null);
    }
}
