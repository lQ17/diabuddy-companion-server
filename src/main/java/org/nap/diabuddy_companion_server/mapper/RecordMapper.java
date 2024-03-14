package org.nap.diabuddy_companion_server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.nap.diabuddy_companion_server.entity.Record;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface RecordMapper extends BaseMapper<Record> {

    @Select("SELECT * FROM user_behavior_records WHERE user_id = #{userId} ORDER BY record_time DESC LIMIT 5")
    List<Record> selectLatestFiveRecords(@Param("userId") Integer userId);

}
