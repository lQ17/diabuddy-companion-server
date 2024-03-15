package org.nap.diabuddy_companion_server.service.impl.recordImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nap.diabuddy_companion_server.entity.record.RecordBloodSugar;
import org.nap.diabuddy_companion_server.mapper.record.RecordBloodSugarMapper;
import org.nap.diabuddy_companion_server.service.record.RecordBloodSugarService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RecordBloodSugarServiceImpl extends ServiceImpl<RecordBloodSugarMapper, RecordBloodSugar> implements RecordBloodSugarService {
    public Float findLastBloodSugarByUserIdAndDate(Integer userId, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        QueryWrapper<RecordBloodSugar> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("blood_sugar_value") // 仅选择血糖值字段
                .eq("user_id", userId) // 筛选特定用户
                .between("record_time", startOfDay, endOfDay) // 筛选指定时间范围
                .orderByDesc("record_time") // 按记录时间降序
                .last("LIMIT 1"); // 限制结果为1条

        RecordBloodSugar record = this.getOne(queryWrapper);
        return record != null ? record.getBloodSugarValue() : null;
    }
}
