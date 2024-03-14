package org.nap.diabuddy_companion_server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.nap.diabuddy_companion_server.entity.Record;

import java.time.LocalDateTime;
import java.util.List;

public interface RecordService extends IService<Record> {

    public List<Record> getLatestFiveRecords(Integer userId);
    public Page<Record> getRecordsPage(int page, int pageSize, Integer userId, String recordType, LocalDateTime startTime, LocalDateTime endTime);
}
