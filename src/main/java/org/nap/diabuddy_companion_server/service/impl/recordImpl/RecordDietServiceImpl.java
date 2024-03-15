package org.nap.diabuddy_companion_server.service.impl.recordImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nap.diabuddy_companion_server.entity.record.RecordDiet;
import org.nap.diabuddy_companion_server.mapper.record.RecordDietMapper;
import org.nap.diabuddy_companion_server.service.record.RecordDietService;
import org.springframework.stereotype.Service;

@Service
public class RecordDietServiceImpl extends ServiceImpl<RecordDietMapper, RecordDiet> implements RecordDietService {
}
