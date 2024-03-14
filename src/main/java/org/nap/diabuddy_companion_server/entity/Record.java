package org.nap.diabuddy_companion_server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data()
@TableName("user_behavior_records")
public class Record implements Serializable {

    @TableId(value = "record_root_id", type = IdType.AUTO)
    private Integer recordRootId;

    private Integer userId;

    private String recordType;

    private Integer recordId;

    private Date recordTime;
}
