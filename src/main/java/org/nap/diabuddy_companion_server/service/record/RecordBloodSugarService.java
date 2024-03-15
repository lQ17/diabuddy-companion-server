package org.nap.diabuddy_companion_server.service.record;

import com.baomidou.mybatisplus.extension.service.IService;
import org.nap.diabuddy_companion_server.entity.record.RecordBloodSugar;

import java.time.LocalDateTime;

public interface RecordBloodSugarService extends IService<RecordBloodSugar> {
    Float findLastBloodSugarByUserIdAndDate(Integer userId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
