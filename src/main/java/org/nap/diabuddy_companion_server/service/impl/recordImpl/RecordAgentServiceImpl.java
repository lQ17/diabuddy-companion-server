package org.nap.diabuddy_companion_server.service.impl.recordImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nap.diabuddy_companion_server.entity.record.RecordAgent;
import org.nap.diabuddy_companion_server.mapper.record.RecordAgentMapper;
import org.nap.diabuddy_companion_server.service.record.RecordAgentService;
import org.springframework.stereotype.Service;

@Service
public class RecordAgentServiceImpl extends ServiceImpl<RecordAgentMapper, RecordAgent> implements RecordAgentService {
    public RecordAgent findLastAgent(Integer userId) {
        QueryWrapper<RecordAgent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId) // 筛选特定用户
                .orderByDesc("record_time") // 按记录时间降序
                .last("LIMIT 1"); // 限制结果为1条
        return this.getOne(queryWrapper);
    }
}
