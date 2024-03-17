package org.nap.diabuddy_companion_server.entity.plan;

import lombok.Data;

@Data
public class PlanInsulinPump {
    private Integer id;
    private Integer planId; // 关联到治疗方案表(treatment_plans)的plan_id
    private Integer userId;
    private Integer totalInsulinPumpBasalRateId; // 关联到胰岛素泵基础率表(insulin_pump_basal_rate)的id
    private Float totalInsulinPumpBasal; // 基础率量
    private String insulinType; // 表示使用的胰岛素类型
    private Float breakfastBolus; // 表示早餐--大剂量注射用量
    private Float lunchBolus; // 表示午餐--大剂量注射用量
    private Float dinnerBolus; // 表示晚餐--大剂量注射用量
    private Float breakfastSquareWaveRate; // 表示早餐--方波的注射率（U/H）
    private Integer breakfastSquareWaveTime; // 表示早餐--方波的注射时长（mins）
    private Float lunchSquareWaveRate; // 表示午餐--方波的注射率（U/H）
    private Integer lunchSquareWaveTime; // 表示午餐--方波的注射时长（mins）
    private Float dinnerSquareWaveRate; // 表示晚餐--方波的注射率（U/H）
    private Integer dinnerSquareWaveTime; // 表示晚餐--方波的注射时长（mins）
}
