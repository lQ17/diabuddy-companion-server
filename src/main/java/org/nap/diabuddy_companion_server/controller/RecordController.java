package org.nap.diabuddy_companion_server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.nap.diabuddy_companion_server.common.R;
import org.nap.diabuddy_companion_server.entity.*;
import org.nap.diabuddy_companion_server.entity.Record;
import org.nap.diabuddy_companion_server.entity.VO.RecordVOForHomePage;
import org.nap.diabuddy_companion_server.entity.VO.RecordVOForListPage;
import org.nap.diabuddy_companion_server.service.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/record")
public class RecordController {

    @Resource
    private RecordService recordService;

    @Resource
    private RecordAgentService recordAgentService;

    @Resource
    private RecordBloodSugarService recordBloodSugarService;

    @Resource
    private RecordDietService recordDietService;

    @Resource
    private RecordExerciseService recordExerciseService;

    @Resource
    private RecordInjectionService recordInjectionService;

    /**
     * 首页-五条最近的记录
     * @param userId
     * @return
     */
    @GetMapping("/get-5-records/{userId}")
    public R<List<RecordVOForHomePage>> getLatestFiveRecords(@PathVariable("userId") Integer userId) {
        try {
            List<Record> records = recordService.getLatestFiveRecords(userId);
            if (records.isEmpty()) {
                return R.error("没有找到记录");
            }
            List<RecordVOForHomePage> recordList = recordToVO(records);
            return R.success(recordList);
        } catch (Exception e) {
            // 日志记录异常
            log.error(e.toString());
            return R.error("获取记录时发生错误");
        }
    }

    /**
     * 根据条件-分页查询
     * @param userId
     * @param recordType
     * @param page
     * @param pageSize
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/list")
    public R<Page<RecordVOForListPage>> getRecordList(
            @RequestParam Integer userId,
            @RequestParam String recordType,
            @RequestParam Integer page,
            @RequestParam Integer pageSize,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {

        Page<Record> recordPage = recordService.getRecordsPage(page, pageSize, userId, recordType, startTime, endTime);

        // 创建VO分页对象
        Page<RecordVOForListPage> voPage = new Page<>(page, pageSize);
        voPage.setTotal(recordPage.getTotal());

        List<RecordVOForListPage> voList = new ArrayList<>();

        //把VO List装填好
        for (Record record : recordPage.getRecords()) {
            RecordVOForListPage vo = transformToVO(record);
            switch (vo.getRecordType()){
                case "agent" -> {
                    RecordAgent recordAgent = recordAgentService.getById(record.getRecordId());
                    vo.setDosage(recordAgent.getDosage().toString());
                    vo.setAgentName(recordAgent.getAgentName());
                }
                case "blood_sugar" -> {
                    RecordBloodSugar recordBloodSugar = recordBloodSugarService.getById(record.getRecordId());
                    vo.setBloodSugarLevel(recordBloodSugar.getBloodSugarValue().toString());
                    vo.setMeasureTime(recordBloodSugar.getMeasureTime());
                }
                case "diet" -> {
                    RecordDiet recordDiet = recordDietService.getById(record.getRecordId());
                    vo.setTotalCarb(recordDiet.getTotalCarb().toString());
                    vo.setMealType(recordDiet.getMealType());
                }
                case "exercise" -> {
                    RecordExercise recordExercise = recordExerciseService.getById(record.getRecordId());
                    vo.setDuration(recordExercise.getDuration().toString());
                    vo.setExerciseType(recordExercise.getExerciseType());
                }
                case "injection" -> {
                    RecordInjection recordInjection = recordInjectionService.getById(record.getRecordId());
                    vo.setInjectionType(recordInjection.getInjectionType());

                    String bolus = Optional.ofNullable(recordInjection.getBolus()).map(Object::toString).orElse(null);
                    vo.setBolus(bolus);

                    String squareWaveTime = Optional.ofNullable(recordInjection.getSquareWaveTime()).map(Object::toString).orElse(null);
                    vo.setSquareWaveTime(squareWaveTime);

                    String squareWaveRate = Optional.ofNullable(recordInjection.getSquareWaveRate()).map(Object::toString).orElse(null);
                    vo.setSquareWaveRate(squareWaveRate);
                }
                default -> {
                }
            }
            voList.add(vo);
        }
        voPage.setRecords(voList);

        return R.success(voPage);
    }

    private List<RecordVOForHomePage> recordToVO(List<Record> records){
        List<RecordVOForHomePage> recordList = new ArrayList<>();
        for (Record record : records) {
            switch (record.getRecordType()) {
                case "agent" -> {
                    RecordAgent recordAgent = recordAgentService.getById(record.getRecordId());
                    recordList.add(agentToVO(recordAgent));
                }
                case "blood_sugar" -> {
                    RecordBloodSugar recordBloodSugar = recordBloodSugarService.getById(record.getRecordId());
                    recordList.add(bloodSugarToVO(recordBloodSugar));
                }
                case "diet" -> {
                    RecordDiet recordDiet = recordDietService.getById(record.getRecordId());
                    recordList.add(dietToVO(recordDiet));
                }
                case "exercise" -> {
                    RecordExercise recordExercise = recordExerciseService.getById(record.getRecordId());
                    recordList.add(exerciseToVO(recordExercise));
                }
                case "injection" -> {
                    RecordInjection recordInjection = recordInjectionService.getById(record.getRecordId());
                    recordList.add(injectionToVO(recordInjection));
                }
                default -> {
                }
            }
        }
        return recordList;
    }

    private RecordVOForHomePage agentToVO(RecordAgent recordAgent){
        return new RecordVOForHomePage(recordAgent.getId().toString(),"agent",recordAgent.getAgentName(),recordAgent.getDosage().toString());
    }

    private RecordVOForHomePage bloodSugarToVO(RecordBloodSugar recordBloodSugar){
        return new RecordVOForHomePage(recordBloodSugar.getId().toString(),"blood_sugar",recordBloodSugar.getMeasureTime(),recordBloodSugar.getBloodSugarValue().toString());
    }

    private RecordVOForHomePage dietToVO(RecordDiet recordDiet){
        String mealTypeInChinese = switch (recordDiet.getMealType()) {
            case "breakfast" -> "早餐";
            case "lunch" -> "午餐";
            case "dinner" -> "晚餐";
            default -> "加餐";
        };
        return new RecordVOForHomePage(recordDiet.getId().toString(),"diet",mealTypeInChinese,recordDiet.getTotalCarb().toString());
    }

    private RecordVOForHomePage exerciseToVO(RecordExercise recordExercise){
        return new RecordVOForHomePage(recordExercise.getId().toString(),"exercise",recordExercise.getExerciseType(),recordExercise.getDuration().toString());
    }

    private RecordVOForHomePage injectionToVO(RecordInjection recordInjection){
        if(recordInjection.getInjectionType().equals("方波")){
            return new RecordVOForHomePage(recordInjection.getId().toString(),"injection",recordInjection.getInjectionType(),recordInjection.getSquareWaveTime().toString()+"分"+recordInjection.getSquareWaveRate().toString());
        }else{
            return new RecordVOForHomePage(recordInjection.getId().toString(),"injection",recordInjection.getInjectionType(),recordInjection.getBolus().toString());
        }
    }

    private RecordVOForListPage transformToVO(Record record) {
        // 根据Record实体转换为RecordVOForListPage实体，根据需要填充属性
        return new RecordVOForListPage(
                record.getRecordRootId(),
                record.getRecordType(),
                record.getRecordTime());
    }
}
