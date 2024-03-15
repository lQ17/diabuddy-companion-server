package org.nap.diabuddy_companion_server.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.nap.diabuddy_companion_server.common.R;
import org.nap.diabuddy_companion_server.entity.Record;
import org.nap.diabuddy_companion_server.entity.RecordAgent;
import org.nap.diabuddy_companion_server.service.RecordAgentService;
import org.nap.diabuddy_companion_server.service.RecordService;
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
public class RecordAgentController {

    @Resource
    private RecordService recordService;

    @Resource
    private RecordAgentService recordAgentService;

    @PostMapping("/agent")
    @Transactional
    public R<Object> addRecord(@RequestBody RecordAgent recordAgent){
        boolean isSaved = recordAgentService.save(recordAgent);

        if (!isSaved) {
            return R.error("未成功记录");
        }

        // 构造一个Record
        Record record = new Record();
        record.setRecordId(recordAgent.getId());
        record.setRecordTime(recordAgent.getRecordTime());
        record.setRecordType("agent");
        record.setUserId(recordAgent.getUserId());

        // 保存 Record 到数据库
         recordService.save(record);

        return R.success(null);

    }

    @PutMapping("/agent")
    @Transactional
    public R<Object> updateRecord(@RequestBody RecordAgent recordAgent){

        if (recordAgent == null || recordAgent.getId() == null) {
            return R.error("请求数据无效");
        }

        boolean isUpdated = recordAgentService.updateById(recordAgent);

        if(isUpdated){
            LambdaQueryWrapper<Record> queryWrapper = new LambdaQueryWrapper<>();
            // 根据 子表id 和 子表类型 确定总表的唯一数据
            queryWrapper.eq(Record::getRecordId,recordAgent.getId());
            queryWrapper.eq(Record::getRecordType,"agent");
            // 获得总表实体
            Record record = recordService.getOne(queryWrapper);
            // 改时间
            record.setRecordTime(recordAgent.getRecordTime());
            // 存总表
            recordService.updateById(record);
        }else{
            return R.error("修改失败，请重试");
        }

        return R.success(null);
    }

    @GetMapping("/user-last-agent/{userId}")
    public R<Object> getLastAgentRecord(@PathVariable("userId") Integer userId){

        Map<String,Object> res = new HashMap<>();
        // 调用服务层方法获取用户最后一条用药记录
        RecordAgent lastRecordAgent = recordAgentService.findLastAgent(userId);

        if (lastRecordAgent != null) {
            res.put("last_agent",lastRecordAgent.getAgentName());
            res.put("last_dosage",lastRecordAgent.getDosage());
            return R.success(res);
        }else {
            return R.success(null);
        }
    }
}
