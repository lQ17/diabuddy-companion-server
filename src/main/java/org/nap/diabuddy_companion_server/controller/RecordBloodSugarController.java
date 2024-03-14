package org.nap.diabuddy_companion_server.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.nap.diabuddy_companion_server.common.R;
import org.nap.diabuddy_companion_server.entity.Record;
import org.nap.diabuddy_companion_server.entity.RecordAgent;
import org.nap.diabuddy_companion_server.entity.RecordBloodSugar;
import org.nap.diabuddy_companion_server.service.RecordBloodSugarService;
import org.nap.diabuddy_companion_server.service.RecordService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
