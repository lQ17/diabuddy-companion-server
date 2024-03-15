package org.nap.diabuddy_companion_server.service.impl.recordImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nap.diabuddy_companion_server.entity.record.Record;
import org.nap.diabuddy_companion_server.mapper.record.RecordMapper;
import org.nap.diabuddy_companion_server.service.record.RecordService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

    public List<Record> getLatestFiveRecords(Integer userId) {
        return baseMapper.selectLatestFiveRecords(userId);
    }

    public Page<Record> getRecordsPage(int page, int pageSize, Integer userId, String recordType, LocalDateTime startTime, LocalDateTime endTime) {
        Page<Record> recordPage = new Page<>(page, pageSize);
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("user_id", userId);

        if (!"all".equals(recordType)) {
            queryWrapper.eq("record_type", recordType);
        }

        if (startTime != null && endTime != null) {
            queryWrapper.between("record_time", startTime, endTime);
        }

        queryWrapper.orderByDesc("record_time");

        return this.page(recordPage, queryWrapper);
    }
}
