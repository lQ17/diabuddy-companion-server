package org.nap.diabuddy_companion_server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("injection_records")
public class RecordInjection implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private String injectionType;

    private Float bolus;

    private Date recordTime;

    private Float squareWaveRate;

    private Integer squareWaveTime;

    private String insulinType;

    private String remark;
}
