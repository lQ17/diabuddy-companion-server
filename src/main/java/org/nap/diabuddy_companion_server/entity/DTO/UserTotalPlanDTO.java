package org.nap.diabuddy_companion_server.entity.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UserTotalPlanDTO {

    private Integer userId; // 此处为userId

    // 控糖参数
    private Float tdd;
    private Float icr;
    private Float isf;
    private Float dayEatingEnergy;
    private Float dayEatingCarb;
    private Float dayEatingProtein;
    private Float dayEatingFat;

    // 治疗方案
    private String treatmentOption;
    private String remark; // 来自plan总表

    // 子对象
    private UserTotalPlanDTO.InsulinPump insulinPump;
    private UserTotalPlanDTO.PreMealAndBasal preMealAndBasal;
    private UserTotalPlanDTO.Premixed premixed;
    private UserTotalPlanDTO.Agent agent;

    @Getter
    @Setter
    public static class InsulinPump {
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

    @Getter
    @Setter
    public static class PreMealAndBasal {
        private Integer id;
        private Integer planId; // 治疗方案表的plan_id
        private String preMealInsulinType; // 餐前胰岛素的类型
        private String basalInsulinType; // 基础胰岛素的类型
        private Float breakfastInsulinDose; // 早餐胰岛素剂量
        private Float lunchInsulinDose; // 午餐胰岛素剂量
        private Float dinnerInsulinDose; // 晚餐胰岛素剂量
        private Float basalInsulinDose; // 基础胰岛素注射的剂量
    }

    @Getter
    @Setter
    public static class Premixed {
        private Integer id;
        private Integer planId; // 关联到治疗方案表的plan_id
        private String premixedInsulinType; // 表示预混胰岛素的类型
        private Float breakfastPremixedDose; // 早餐时的预混胰岛素剂量
        private Float lunchPremixedDose; // 午餐时的预混胰岛素剂量
        private Float dinnerPremixedDose; // 晚餐时的预混胰岛素剂量
    }

    @Getter
    @Setter
    public static class Agent {
        private Integer id;
        private Integer planId;
        private String agentName;
        private Float agentDosage;
    }
}
