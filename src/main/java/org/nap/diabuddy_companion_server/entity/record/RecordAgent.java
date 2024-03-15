package org.nap.diabuddy_companion_server.entity.record;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("agents_records")
public class RecordAgent implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private String agentName;

    private Integer dosage;

    private Date recordTime;

    private String remark;
}
