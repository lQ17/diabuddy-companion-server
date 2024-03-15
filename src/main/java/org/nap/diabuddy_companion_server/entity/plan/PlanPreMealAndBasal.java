package org.nap.diabuddy_companion_server.entity.plan;

import lombok.Data;

@Data
public class PlanPreMealAndBasal {

    private Integer id;
    private Integer planId; // 治疗方案表的plan_id
    private String preMealInsulinType; // 餐前胰岛素的类型
    private String basalInsulinType; // 基础胰岛素的类型
    private Float breakfastInsulinDose; // 早餐胰岛素剂量
    private Float lunchInsulinDose; // 午餐胰岛素剂量
    private Float dinnerInsulinDose; // 晚餐胰岛素剂量
    private Float basalInsulinDose; // 基础胰岛素注射的剂量
}
