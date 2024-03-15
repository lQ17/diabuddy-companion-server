package org.nap.diabuddy_companion_server.controller.record;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.nap.diabuddy_companion_server.common.R;
import org.nap.diabuddy_companion_server.entity.record.Record;
import org.nap.diabuddy_companion_server.entity.record.RecordBloodSugar;
import org.nap.diabuddy_companion_server.service.record.RecordBloodSugarService;
import org.nap.diabuddy_companion_server.service.record.RecordService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/record")
public class RecordBloodSugarController {

    @Resource
    private RecordService recordService;

    @Resource
    private RecordBloodSugarService recordBloodSugarService;

    @PostMapping("/blood-sugar")
    @Transactional
    public R<Object> addRecord(@RequestBody RecordBloodSugar recordBloodSugar){
        boolean isSaved = recordBloodSugarService.save(recordBloodSugar);

        if (!isSaved) {
            return R.error("未成功记录");
        }

        // 构造一个Record
        Record record = new Record();
        record.setRecordId(recordBloodSugar.getId());
        record.setRecordTime(recordBloodSugar.getRecordTime());
        record.setRecordType("blood_sugar");
        record.setUserId(recordBloodSugar.getUserId());

        // 保存 Record 到数据库
        recordService.save(record);

        return R.success(null);

    }

    @PutMapping("/blood-sugar")
    @Transactional
    public R<Object> updateRecord(@RequestBody RecordBloodSugar recordBloodSugar){

        if (recordBloodSugar == null || recordBloodSugar.getId() == null) {
            return R.error("请求数据无效");
        }

        boolean isUpdated = recordBloodSugarService.updateById(recordBloodSugar);

        if(isUpdated){
            LambdaQueryWrapper<Record> queryWrapper = new LambdaQueryWrapper<>();
            // 根据 子表id 和 子表类型 确定总表的唯一数据
            queryWrapper.eq(Record::getRecordId,recordBloodSugar.getId());
            queryWrapper.eq(Record::getRecordType,"blood_sugar");
            // 获得总表实体
            Record record = recordService.getOne(queryWrapper);
            // 改时间
            record.setRecordTime(recordBloodSugar.getRecordTime());
            // 存总表
            recordService.updateById(record);
        }else{
            return R.error("修改失败，请重试");
        }

        return R.success(null);
    }

    @GetMapping("/user-today-last-blood-sugar/{userId}")
    public R<Object> getTodayLastBloodSugar(@PathVariable("userId") Integer userId) {
        // 获取当天的日期范围
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);


        Map<String,Float> bloodSugarValue = new HashMap<>();
        // 调用服务层方法获取用户当天的最后一条血糖记录
        bloodSugarValue.put("bloodSugarValue",recordBloodSugarService.findLastBloodSugarByUserIdAndDate(userId, startOfDay, endOfDay));

        if (bloodSugarValue.get("bloodSugarValue") != null) {
            return R.success(bloodSugarValue);
        } else {
            return R.error("未找到用户今天的血糖记录");
        }
    }
}
