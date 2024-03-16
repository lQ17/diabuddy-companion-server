package org.nap.diabuddy_companion_server.mapper.plan;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.nap.diabuddy_companion_server.entity.plan.InsulinPumpBasalRate;

@Mapper
public interface InsulinPumpBasalRateMapper extends BaseMapper<InsulinPumpBasalRate> {

    @Select("SELECT * FROM insulin_pump_basal_rate WHERE user_id = #{userId} ORDER BY start_time DESC LIMIT 1")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endedTime", column = "ended_time"),
            @Result(property = "rate_0000", column = "rate_0000"),
            @Result(property = "rate_0030", column = "rate_0030"),
            @Result(property = "rate_0100", column = "rate_0100"),
            @Result(property = "rate_0130", column = "rate_0130"),
            @Result(property = "rate_0200", column = "rate_0200"),
            @Result(property = "rate_0230", column = "rate_0230"),
            @Result(property = "rate_0300", column = "rate_0300"),
            @Result(property = "rate_0330", column = "rate_0330"),
            @Result(property = "rate_0400", column = "rate_0400"),
            @Result(property = "rate_0430", column = "rate_0430"),
            @Result(property = "rate_0500", column = "rate_0500"),
            @Result(property = "rate_0530", column = "rate_0530"),
            @Result(property = "rate_0600", column = "rate_0600"),
            @Result(property = "rate_0630", column = "rate_0630"),
            @Result(property = "rate_0700", column = "rate_0700"),
            @Result(property = "rate_0730", column = "rate_0730"),
            @Result(property = "rate_0800", column = "rate_0800"),
            @Result(property = "rate_0830", column = "rate_0830"),
            @Result(property = "rate_0900", column = "rate_0900"),
            @Result(property = "rate_0930", column = "rate_0930"),
            @Result(property = "rate_1000", column = "rate_1000"),
            @Result(property = "rate_1030", column = "rate_1030"),
            @Result(property = "rate_1100", column = "rate_1100"),
            @Result(property = "rate_1130", column = "rate_1130"),
            @Result(property = "rate_1200", column = "rate_1200"),
            @Result(property = "rate_1230", column = "rate_1230"),
            @Result(property = "rate_1300", column = "rate_1300"),
            @Result(property = "rate_1330", column = "rate_1330"),
            @Result(property = "rate_1400", column = "rate_1400"),
            @Result(property = "rate_1430", column = "rate_1430"),
            @Result(property = "rate_1500", column = "rate_1500"),
            @Result(property = "rate_1530", column = "rate_1530"),
            @Result(property = "rate_1600", column = "rate_1600"),
            @Result(property = "rate_1630", column = "rate_1630"),
            @Result(property = "rate_1700", column = "rate_1700"),
            @Result(property = "rate_1730", column = "rate_1730"),
            @Result(property = "rate_1800", column = "rate_1800"),
            @Result(property = "rate_1830", column = "rate_1830"),
            @Result(property = "rate_1900", column = "rate_1900"),
            @Result(property = "rate_1930", column = "rate_1930"),
            @Result(property = "rate_2000", column = "rate_2000"),
            @Result(property = "rate_2030", column = "rate_2030"),
            @Result(property = "rate_2100", column = "rate_2100"),
            @Result(property = "rate_2130", column = "rate_2130"), //44
            @Result(property = "rate_2200", column = "rate_2200"),
            @Result(property = "rate_2230", column = "rate_2230"),
            @Result(property = "rate_2300", column = "rate_2300"),
            @Result(property = "rate_2330", column = "rate_2330"),
            // 确保每个数据库字段都映射到相应的 Java 属性
    })
    InsulinPumpBasalRate findLatestByUserId(@Param("userId") Integer userId);
}
