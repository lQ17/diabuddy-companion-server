package org.nap.diabuddy_companion_server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.nap.diabuddy_companion_server.entity.Record;
import org.nap.diabuddy_companion_server.entity.RecordBloodSugar;

import java.time.LocalDateTime;

public interface RecordBloodSugarService extends IService<RecordBloodSugar> {
    Float findLastBloodSugarByUserIdAndDate(Integer userId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
