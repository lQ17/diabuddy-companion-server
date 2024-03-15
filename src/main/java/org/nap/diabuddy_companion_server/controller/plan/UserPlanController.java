package org.nap.diabuddy_companion_server.controller.plan;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.nap.diabuddy_companion_server.common.R;
import org.nap.diabuddy_companion_server.entity.DTO.PlanDTO;
import org.nap.diabuddy_companion_server.entity.plan.InsulinPumpBasalRate;
import org.nap.diabuddy_companion_server.entity.user.User;
import org.nap.diabuddy_companion_server.service.plan.InsulinPumpBasalRateService;
import org.nap.diabuddy_companion_server.service.plan.UserPlanService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/plan")
public class UserPlanController {

    @Resource
    private UserPlanService userPlanService;

    @Resource
    private InsulinPumpBasalRateService insulinPumpBasalRateService;

    @GetMapping("/tdd/{userId}")
    public R<Map> getTDD(@PathVariable("userId") Integer userId){

        User user = userPlanService.getById(userId);

        HashMap<String, Float> res = new HashMap<>();

        res.put("tdd",user.getTdd());

        return R.success(res);
    }

    @PostMapping("/tdd")//
    public R<Object> addTDD(@RequestBody PlanDTO planDTO){

        User user = userPlanService.getById(planDTO.getUserId());

        if(user == null){
            return R.error("保存失败");
        }

        user.setTdd(planDTO.getTdd());

        userPlanService.updateById(user);

        return R.success(null);
    }

    @PostMapping("/icr")//
    public R<Object> addICR(@RequestBody PlanDTO planDTO){

        User user = userPlanService.getById(planDTO.getUserId());

        if(user == null){
            return R.error("保存失败");
        }

        user.setIcr(planDTO.getIcr());

        userPlanService.updateById(user);

        return R.success(null);
    }

    @PostMapping("/isf")//
    public R<Object> addISF(@RequestBody PlanDTO planDTO){

        User user = userPlanService.getById(planDTO.getUserId());

        if(user == null){
            return R.error("保存失败");
        }

        user.setIsf(planDTO.getIsf());

        userPlanService.updateById(user);

        return R.success(null);
    }

    @PostMapping("/pump-basal-rate")
    public R<Object> addPumpBasalRate(@RequestBody InsulinPumpBasalRate insulinPumpBasalRate){

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

    @PostMapping("/day-eating")//
    public R<Object> addDayEating(@RequestBody PlanDTO planDTO){

        User user = userPlanService.getById(planDTO.getUserId());

        if(user == null){
            return R.error("保存失败");
        }

        user.setDayEatingEnergy(planDTO.getDayEatingEnergy());
        user.setDayEatingCarb(planDTO.getDayEatingCarb());
        user.setDayEatingFat(planDTO.getDayEatingFat());
        user.setDayEatingProtein(planDTO.getDayEatingProtein());

        userPlanService.updateById(user);

        return R.success(null);
    }
}
