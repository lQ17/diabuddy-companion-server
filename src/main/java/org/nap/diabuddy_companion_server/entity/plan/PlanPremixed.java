package org.nap.diabuddy_companion_server.entity.plan;

import lombok.Data;

@Data
public class PlanPremixed {

    private Integer id;
    private Integer planId; // 关联到治疗方案表的plan_id
    private String premixedInsulinType; // 表示预混胰岛素的类型
    private Float breakfastPremixedDose; // 早餐时的预混胰岛素剂量
    private Float lunchPremixedDose; // 午餐时的预混胰岛素剂量
    private Float dinnerPremixedDose; // 晚餐时的预混胰岛素剂量
}
