package org.nap.diabuddy_companion_server.controller.plan;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.nap.diabuddy_companion_server.common.R;
import org.nap.diabuddy_companion_server.entity.VO.UserTotalPlanVO;
import org.nap.diabuddy_companion_server.entity.plan.*;
import org.nap.diabuddy_companion_server.entity.user.User;
import org.nap.diabuddy_companion_server.service.plan.*;
import org.nap.diabuddy_companion_server.service.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/plan")
public class PlanController {

    @Resource
    private UserService userService;

    @Resource
    private PlanService planService;

    @Resource
    private PlanAgentService planAgentService;

    @Resource
    private PlanInsulinPumpService planInsulinPumpService;

    @Resource
    private PlanPreMealAndBasalService planPreMealAndBasalService;

    @Resource
    private PlanPremixedService planPremixedService;


    @GetMapping("/medical-info/{userId}")
    public R<UserTotalPlanVO> getUserTotalMedicalInfo(@PathVariable("userId") Integer userId){
        User user = userService.getById(userId);

        if(user == null){
            return R.error("用户不存在");
        }

        // 创建一个vo
        UserTotalPlanVO vo = new UserTotalPlanVO();

        // 设置user表中的参数
        vo.setTdd(user.getTdd());
        vo.setIcr(user.getIcr());
        vo.setIsf(user.getIsf());
        vo.setDayEatingEnergy(user.getDayEatingEnergy());
        vo.setDayEatingCarb(user.getDayEatingCarb());
        vo.setDayEatingFat(user.getDayEatingFat());
        vo.setDayEatingProtein(user.getDayEatingProtein());

        // 获取plan总表
        LambdaQueryWrapper<Plan> queryWrapper = new LambdaQueryWrapper<>();

        // 根据userId查plan总表，获得一个plan对象
        queryWrapper.eq(Plan::getUserId,user.getId());
        Plan plan = planService.getOne(queryWrapper);

        // 设置remark、treatmentOption字段
        vo.setRemark(plan.getRemark());
        vo.setTreatmentOption(plan.getTreatmentOption());

        // 药比较特殊，不是互斥的方案
        LambdaQueryWrapper<PlanAgent> agentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        agentLambdaQueryWrapper.eq(PlanAgent::getPlanId, plan.getPlanId());
        PlanAgent planAgent = planAgentService.getOne(agentLambdaQueryWrapper);
        if(planAgent != null){
            vo.setAgent(getAgentObject(planAgent));
        }


        // 根据plan对象的treatmentOption查对应表
        switch (plan.getTreatmentOption()) {
            // 根据planId查子表
            case "plan_insulin_pump" -> {
                LambdaQueryWrapper<PlanInsulinPump> insulinPumpLambdaQueryWrapper = new LambdaQueryWrapper<>();
                insulinPumpLambdaQueryWrapper.eq(PlanInsulinPump::getPlanId, plan.getPlanId());
                PlanInsulinPump planInsulinPump = planInsulinPumpService.getOne(insulinPumpLambdaQueryWrapper);
                vo.setInsulinPump(getInsulinPumpObject(planInsulinPump));
                return R.success(vo);
            }
            case "plan_pre_meal_and_basal" -> {
                LambdaQueryWrapper<PlanPreMealAndBasal> preMealAndBasalLambdaQueryWrapper = new LambdaQueryWrapper<>();
                preMealAndBasalLambdaQueryWrapper.eq(PlanPreMealAndBasal::getPlanId, plan.getPlanId());
                PlanPreMealAndBasal planPreMealAndBasal = planPreMealAndBasalService.getOne(preMealAndBasalLambdaQueryWrapper);
                vo.setPreMealAndBasal(getPreMealAndBasalObject(planPreMealAndBasal));
                return R.success(vo);
            }
            case "plan_premixed" -> {
                LambdaQueryWrapper<PlanPremixed> premixedLambdaQueryWrapper = new LambdaQueryWrapper<>();
                premixedLambdaQueryWrapper.eq(PlanPremixed::getPlanId, plan.getPlanId());
                PlanPremixed planPremixed = planPremixedService.getOne(premixedLambdaQueryWrapper);
                vo.setPremixed(getPremixedObject(planPremixed));
                return R.success(vo);
            }
        }

        return R.success(vo);

    }

    private UserTotalPlanVO.InsulinPump getInsulinPumpObject(PlanInsulinPump planInsulinPump) {
        UserTotalPlanVO.InsulinPump insulinPump = new UserTotalPlanVO.InsulinPump();

        insulinPump.setId(planInsulinPump.getId());
        insulinPump.setPlanId(planInsulinPump.getPlanId());
        insulinPump.setUserId(planInsulinPump.getUserId());
        insulinPump.setTotalInsulinPumpBasalRateId(planInsulinPump.getTotalInsulinPumpBasalRateId());
        insulinPump.setTotalInsulinPumpBasal(planInsulinPump.getTotalInsulinPumpBasal());
        insulinPump.setInsulinType(planInsulinPump.getInsulinType());
        insulinPump.setBreakfastBolus(planInsulinPump.getBreakfastBolus());
        insulinPump.setLunchBolus(planInsulinPump.getLunchBolus());
        insulinPump.setDinnerBolus(planInsulinPump.getDinnerBolus());
        insulinPump.setBreakfastSquareWaveRate(planInsulinPump.getBreakfastSquareWaveRate());
        insulinPump.setBreakfastSquareWaveTime(planInsulinPump.getBreakfastSquareWaveTime());
        insulinPump.setLunchSquareWaveRate(planInsulinPump.getLunchSquareWaveRate());
        insulinPump.setLunchSquareWaveTime(planInsulinPump.getLunchSquareWaveTime());
        insulinPump.setDinnerSquareWaveRate(planInsulinPump.getDinnerSquareWaveRate());
        insulinPump.setDinnerSquareWaveTime(planInsulinPump.getDinnerSquareWaveTime());

        return insulinPump;
    }

    private UserTotalPlanVO.PreMealAndBasal getPreMealAndBasalObject(PlanPreMealAndBasal planPreMealAndBasal) {
        UserTotalPlanVO.PreMealAndBasal preMealAndBasal = new UserTotalPlanVO.PreMealAndBasal();

        preMealAndBasal.setId(planPreMealAndBasal.getId());
        preMealAndBasal.setPlanId(planPreMealAndBasal.getPlanId());
        preMealAndBasal.setPreMealInsulinType(planPreMealAndBasal.getPreMealInsulinType());
        preMealAndBasal.setBasalInsulinType(planPreMealAndBasal.getBasalInsulinType());
        preMealAndBasal.setBreakfastInsulinDose(planPreMealAndBasal.getBreakfastInsulinDose());
        preMealAndBasal.setLunchInsulinDose(planPreMealAndBasal.getLunchInsulinDose());
        preMealAndBasal.setDinnerInsulinDose(planPreMealAndBasal.getDinnerInsulinDose());
        preMealAndBasal.setBasalInsulinDose(planPreMealAndBasal.getBasalInsulinDose());

        return preMealAndBasal;
    }

    private UserTotalPlanVO.Premixed getPremixedObject(PlanPremixed planPremixed) {
        UserTotalPlanVO.Premixed premixed = new UserTotalPlanVO.Premixed();

        premixed.setId(planPremixed.getId());
        premixed.setPlanId(planPremixed.getPlanId());
        premixed.setPremixedInsulinType(planPremixed.getPremixedInsulinType());
        premixed.setBreakfastPremixedDose(planPremixed.getBreakfastPremixedDose());
        premixed.setLunchPremixedDose(planPremixed.getLunchPremixedDose());
        premixed.setDinnerPremixedDose(planPremixed.getDinnerPremixedDose());

        return premixed;
    }

    private UserTotalPlanVO.Agent getAgentObject(PlanAgent planAgent) {
        UserTotalPlanVO.Agent agent = new UserTotalPlanVO.Agent();

        agent.setId(planAgent.getId());
        agent.setPlanId(planAgent.getPlanId());
        agent.setAgentName(planAgent.getAgentName());
        agent.setAgentDosage(planAgent.getAgentDosage());

        return agent;
    }




}
