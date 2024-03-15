package org.nap.diabuddy_companion_server.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.nap.diabuddy_companion_server.common.R;
import org.nap.diabuddy_companion_server.entity.Record;
import org.nap.diabuddy_companion_server.entity.RecordBloodSugar;
import org.nap.diabuddy_companion_server.entity.RecordDiet;
import org.nap.diabuddy_companion_server.service.RecordDietService;
import org.nap.diabuddy_companion_server.service.RecordService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/record")
public class RecordDietController {

    @Resource
    private RecordService recordService;

    @Resource
    private RecordDietService recordDietService;

    @PostMapping("/diet")
    @Transactional
    public R<Object> addRecord(@RequestBody RecordDiet recordDiet){
        boolean isSaved = recordDietService.save(recordDiet);

        if (!isSaved) {
            return R.error("未成功记录");
        }

        // 构造一个Record
        Record record = new Record();
        record.setRecordId(recordDiet.getId());
        record.setRecordTime(recordDiet.getRecordTime());
        record.setRecordType("diet");
        record.setUserId(recordDiet.getUserId());

        // 保存 Record 到数据库
        recordService.save(record);

        return R.success(null);

    }

    @PutMapping("/diet")
    @Transactional
    public R<Object> updateRecord(@RequestBody RecordDiet recordDiet){

        if (recordDiet == null || recordDiet.getId() == null) {
            return R.error("请求数据无效");
        }

        boolean isUpdated = recordDietService.updateById(recordDiet);

        if(isUpdated){
            LambdaQueryWrapper<Record> queryWrapper = new LambdaQueryWrapper<>();
            // 根据 子表id 和 子表类型 确定总表的唯一数据
            queryWrapper.eq(Record::getRecordId,recordDiet.getId());
            queryWrapper.eq(Record::getRecordType,"diet");
            // 获得总表实体
            Record record = recordService.getOne(queryWrapper);
            // 改时间
            record.setRecordTime(recordDiet.getRecordTime());
            // 存总表
            recordService.updateById(record);
        }else{
            return R.error("修改失败，请重试");
        }

        return R.success(null);
    }
}
