package org.nap.diabuddy_companion_server.controller.plan;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.nap.diabuddy_companion_server.common.R;
import org.nap.diabuddy_companion_server.entity.DTO.UserTotalPlanDTO;
import org.nap.diabuddy_companion_server.entity.VO.UserTotalPlanVO;
import org.nap.diabuddy_companion_server.entity.plan.*;
import org.nap.diabuddy_companion_server.entity.user.User;
import org.nap.diabuddy_companion_server.service.plan.*;
import org.nap.diabuddy_companion_server.service.user.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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

    @Resource
    private InsulinPumpBasalRateService insulinPumpBasalRateService;


    @GetMapping("/medical-info/{userId}")
    @Transactional
    public R<UserTotalPlanVO> getUserTotalMedicalInfo(@PathVariable("userId") Integer userId){
        User user = userService.getById(userId);

        if(user == null){
            return R.error("用户不存在");
        }

        // 创建一个vo
        UserTotalPlanVO vo = new UserTotalPlanVO();

        // 设置user表中的参数
        vo.setId(user.getId());
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

                // 获取最后一张 基础率表
                InsulinPumpBasalRate lastRate = insulinPumpBasalRateService.findLatestByUserId(planInsulinPump.getUserId());

                // 计算基础率总量和id
                float sumOfRates = lastRate.sumOfRates();
                Integer lastRateId = lastRate.getId();

                // 基础率表id和总量  存入planInsulinPump
                planInsulinPump.setTotalInsulinPumpBasal(sumOfRates);
                planInsulinPump.setTotalInsulinPumpBasalRateId(lastRateId);
                planInsulinPumpService.updateById(planInsulinPump);


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

    @PostMapping("/medical-info")
    @Transactional
    public R<Object> updateUserTotalMedicalInfo(@RequestBody UserTotalPlanDTO userTotalPlanDTO){

        // 更新tdd等控糖参数 -- User表
        Boolean isUpdatedUser = updateBaseDiaInfo(userTotalPlanDTO);

        if(!isUpdatedUser){
            log.info("更新User表失败");
            return R.error("更新失败，请重试");
        }

        // 根据userTotalPlanDTO中userId -- Plan表 -- 懒得写service层了
        Integer planId = updatePlanByUserId(userTotalPlanDTO);

        if(planId <= 0){
            log.info("更新Plan表失败");
            return R.error("更新失败，请重试");
        }

        // 根据有无agent，更新agent
        if(userTotalPlanDTO.getAgent() != null){
            Boolean isUpdatedAgent = updateAgentByPlanId(userTotalPlanDTO.getAgent(), planId);
            if(!isUpdatedAgent){
                log.info("更新PlanAgent表失败");
                return R.error("更新失败，请重试");
            }
        }

        // 根据userTotalPlanDTO中treatmentOption的值，选择更新互斥的三张表
        switch (userTotalPlanDTO.getTreatmentOption()) {
            case "plan_insulin_pump" -> {
                Boolean isUpdated = updateInsulinPumpByPlanId(userTotalPlanDTO, planId);
                if(!isUpdated){
                    log.info("更新PlanInsulinPump表失败");
                    return R.error("更新失败，请重试");
                }
            }
            case "plan_pre_meal_and_basal" -> {
                Boolean isUpdated = updatePreMealAndBasalByPlanId(userTotalPlanDTO.getPreMealAndBasal(), planId);
                if(!isUpdated){
                    log.info("更新PlanPreMealAndBasal表失败");
                    return R.error("更新失败，请重试");
                }
            }
            case "plan_premixed" -> {
                Boolean isUpdated = updatePremixedByPlanId(userTotalPlanDTO.getPremixed(), planId);
                if(!isUpdated){
                    log.info("更新PlanPremixed表失败");
                    return R.error("更新失败，请重试");
                }
            }
        }

        return R.success(null);
    }


    @GetMapping("/pump-basal-rate-manual/{userId}")
    @Transactional
    public R<InsulinPumpBasalRate> getPumpBasalRateInDiaInfoPage(@PathVariable Integer userId) {
        InsulinPumpBasalRate latestByUserId = insulinPumpBasalRateService.findLatestByUserId(userId);

        if (latestByUserId == null) {
            return R.error("您还没有基础率记录，请先设置吧");
        }

        return R.success(latestByUserId);
    }

    @PostMapping("/pump-basal-rate-manual")
    @Transactional
    public R<Object> updatePumpBasalRateInDiaInfoPage(@RequestBody InsulinPumpBasalRate insulinPumpBasalRate){

        // 1. 先查一下，之前有没有存过
        InsulinPumpBasalRate lastRate = insulinPumpBasalRateService.findLastRate(insulinPumpBasalRate.getUserId());

        // 2. 如果有，把startTime距现在最近的那一条记录的endedTime改为现在
        if (lastRate != null) {
            lastRate.setEndedTime(LocalDateTime.now());
            insulinPumpBasalRateService.updateById(lastRate);
        }

        // 3. 把这一条记录的startTime设置为现在
        insulinPumpBasalRate.setStartTime(LocalDateTime.now());

        // 4. 保存这条记录
        boolean isSaved = insulinPumpBasalRateService.save(insulinPumpBasalRate);

        if (isSaved) {
            return R.success(null);
        } else {
            return R.error("记录保存失败");
        }
    }


    // 包装VO的子对象

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

    // 包装DTO的子对象

    private Boolean updateBaseDiaInfo(UserTotalPlanDTO userTotalPlanDTO){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId,userTotalPlanDTO.getUserId());
        User user = userService.getOne(queryWrapper);

        //设置控糖参数 -- 7个
        user.setTdd(userTotalPlanDTO.getTdd());
        user.setIcr(userTotalPlanDTO.getIcr());
        user.setIsf(userTotalPlanDTO.getIsf());
        user.setDayEatingEnergy(userTotalPlanDTO.getDayEatingEnergy());
        user.setDayEatingCarb(userTotalPlanDTO.getDayEatingCarb());
        user.setDayEatingProtein(userTotalPlanDTO.getDayEatingProtein());
        user.setDayEatingFat(userTotalPlanDTO.getDayEatingFat());

        return userService.updateById(user);
    }

    private Integer updatePlanByUserId(UserTotalPlanDTO userTotalPlanDTO){

        LambdaQueryWrapper<Plan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Plan::getUserId, userTotalPlanDTO.getUserId());
        Plan plan = planService.getOne(queryWrapper);

        if (plan == null) {
            plan = new Plan();
        }

        // 设置参数
        plan.setUserId(userTotalPlanDTO.getUserId());
        plan.setTreatmentOption(userTotalPlanDTO.getTreatmentOption());
        plan.setRemark(userTotalPlanDTO.getRemark());

        planService.updateById(plan);

        return plan.getPlanId();
    }

    private Boolean updateAgentByPlanId(UserTotalPlanDTO.Agent agent, Integer planId){


        LambdaQueryWrapper<PlanAgent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PlanAgent::getPlanId,planId);
        PlanAgent planAgent = planAgentService.getOne(queryWrapper);

        boolean isNewData;
        if (planAgent == null) {
            planAgent = new PlanAgent();
            isNewData = true;
        }else {
            isNewData = false;
        }

        planAgent.setPlanId(planId);
        planAgent.setAgentName(agent.getAgentName());
        planAgent.setAgentDosage(agent.getAgentDosage());

        if(isNewData){
            return planAgentService.save(planAgent);
        }else{
            return planAgentService.updateById(planAgent);
        }

    }

    private Boolean updateInsulinPumpByPlanId(UserTotalPlanDTO userTotalPlanDTO, Integer planId) {
        UserTotalPlanDTO.InsulinPump insulinPump = userTotalPlanDTO.getInsulinPump();
        LambdaQueryWrapper<PlanInsulinPump> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PlanInsulinPump::getPlanId, planId);
        PlanInsulinPump one = planInsulinPumpService.getOne(queryWrapper);

        boolean isNewData;
        if (one == null) {
            one = new PlanInsulinPump();
            isNewData = true;
        }else {
            isNewData = false;
        }

        // 设置
        one.setPlanId(planId);
        one.setUserId(userTotalPlanDTO.getUserId());
        // 上传时不保存基础率表的内容、展示时在计算，并展示
        //        one.setTotalInsulinPumpBasalRateId();
//        one.setTotalInsulinPumpBasal();
        one.setInsulinType(insulinPump.getInsulinType());
        one.setBreakfastBolus(insulinPump.getBreakfastBolus());
        one.setBreakfastSquareWaveRate(insulinPump.getBreakfastSquareWaveRate());
        one.setBreakfastSquareWaveTime(insulinPump.getBreakfastSquareWaveTime());
        one.setLunchBolus(insulinPump.getLunchBolus());
        one.setLunchSquareWaveRate(insulinPump.getLunchSquareWaveRate());
        one.setLunchSquareWaveTime(insulinPump.getLunchSquareWaveTime());
        one.setDinnerBolus(insulinPump.getDinnerBolus());
        one.setDinnerSquareWaveRate(insulinPump.getDinnerSquareWaveRate());
        one.setDinnerSquareWaveTime(insulinPump.getDinnerSquareWaveTime());

        if(isNewData){
            return planInsulinPumpService.save(one);
        }else {
            UpdateWrapper<PlanInsulinPump> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("plan_id", one.getPlanId());
            // 使用set方法显式设置字段，可以是null
            updateWrapper.set("insulin_type", one.getInsulinType());
            updateWrapper.set("breakfast_bolus", one.getBreakfastBolus());
            updateWrapper.set("breakfast_square_wave_rate", one.getBreakfastSquareWaveRate());
            updateWrapper.set("breakfast_square_wave_time", one.getBreakfastSquareWaveTime());
            updateWrapper.set("lunch_bolus", one.getLunchBolus());
            updateWrapper.set("lunch_square_wave_rate", one.getLunchSquareWaveRate());
            updateWrapper.set("lunch_square_wave_time", one.getLunchSquareWaveTime());
            updateWrapper.set("dinner_bolus", one.getDinnerBolus());
            updateWrapper.set("dinner_square_wave_rate", one.getDinnerSquareWaveRate());
            updateWrapper.set("dinner_square_wave_time", one.getDinnerSquareWaveTime());

            return planInsulinPumpService.update(one, updateWrapper);
        }
    }

    private Boolean updatePreMealAndBasalByPlanId(UserTotalPlanDTO.PreMealAndBasal planPreMealAndBasal, Integer planId) {
        LambdaQueryWrapper<PlanPreMealAndBasal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PlanPreMealAndBasal::getPlanId, planId);
        PlanPreMealAndBasal one = planPreMealAndBasalService.getOne(queryWrapper);

        boolean isNewData;
        if (one == null) {
            one = new PlanPreMealAndBasal();
            isNewData = true;
        }else {
            isNewData = false;
        }

        one.setPlanId(planId);
        one.setPreMealInsulinType(planPreMealAndBasal.getPreMealInsulinType());
        one.setBasalInsulinType(planPreMealAndBasal.getBasalInsulinType());
        one.setBreakfastInsulinDose(planPreMealAndBasal.getBreakfastInsulinDose());
        one.setLunchInsulinDose(planPreMealAndBasal.getLunchInsulinDose());
        one.setDinnerInsulinDose(planPreMealAndBasal.getDinnerInsulinDose());
        one.setBasalInsulinDose(planPreMealAndBasal.getBasalInsulinDose());

        if(isNewData){
            return planPreMealAndBasalService.save(one);
        }else{
            return planPreMealAndBasalService.updateById(one);
        }
    }

    private Boolean updatePremixedByPlanId(UserTotalPlanDTO.Premixed planPremixed, Integer planId) {
        LambdaQueryWrapper<PlanPremixed> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PlanPremixed::getPlanId, planId);
        PlanPremixed one = planPremixedService.getOne(queryWrapper);

        boolean isNewData;
        if(one == null){
            one = new PlanPremixed();
            isNewData = true;
        }else{
            isNewData = false;
        }

        one.setPlanId(planId);
        one.setPremixedInsulinType(planPremixed.getPremixedInsulinType());
        one.setBreakfastPremixedDose(planPremixed.getBreakfastPremixedDose());
        one.setLunchPremixedDose(planPremixed.getLunchPremixedDose());
        one.setDinnerPremixedDose(planPremixed.getDinnerPremixedDose());

        if(isNewData){
            return planPremixedService.save(one);
        }else{
            return planPremixedService.updateById(one);
        }
    }



}
