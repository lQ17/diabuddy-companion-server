package org.nap.diabuddy_companion_server.entity.plan;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("treatment_plans")
public class Plan {

    @TableId(value = "plan_id", type = IdType.AUTO)
    private Integer planId;
    private Integer userId;
    private String treatmentOption;
    private String remark;

}
