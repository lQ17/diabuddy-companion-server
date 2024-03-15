package org.nap.diabuddy_companion_server.controller.record;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.nap.diabuddy_companion_server.common.R;
import org.nap.diabuddy_companion_server.entity.record.*;
import org.nap.diabuddy_companion_server.entity.record.Record;
import org.nap.diabuddy_companion_server.entity.VO.RecordVOForDetail;
import org.nap.diabuddy_companion_server.entity.VO.RecordVOForHomePage;
import org.nap.diabuddy_companion_server.entity.VO.RecordVOForListPage;
import org.nap.diabuddy_companion_server.service.record.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
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

    /**
     * 删除、总表和对应子表
     * @param recordRootId
     * @return
     */
    @Transactional
    @DeleteMapping("/delete/{recordRootId}")
    public R<Object> deleteRecordByRootId(@PathVariable("recordRootId") Integer recordRootId){
        Record record = recordService.getById(recordRootId);

        if(record == null){
            return R.error("该记录不存在");
        }

        //删除总表记录、子表记录
        recordService.removeById(recordRootId);
        switch (record.getRecordType()) {
            case "agent" -> recordAgentService.removeById(record.getRecordId());
            case "blood_sugar" -> recordBloodSugarService.removeById(record.getRecordId());
            case "diet" -> recordDietService.removeById(record.getRecordId());
            case "exercise" -> recordExerciseService.removeById(record.getRecordId());
            case "injection" -> recordInjectionService.removeById(record.getRecordId());
        }

        return R.success(null);
    }

    /**
     * 详情页的数据查看
     * @param recordRootId
     * @return
     */
    @GetMapping("/detail/{recordRootId}")
    public R<RecordVOForDetail> getRecordDetailByRootId(@PathVariable("recordRootId") Integer recordRootId){

        Record record = recordService.getById(recordRootId);

        if(record == null){
            return R.error("记录不存在");
        }

        RecordVOForDetail vo = new RecordVOForDetail();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeString = sdf.format(record.getRecordTime());

        vo.setRecordType(record.getRecordType());
        vo.setRecordTime(timeString);

        RecordVOForDetail.TypeDetail typeDetail = new RecordVOForDetail.TypeDetail();

        //公共值
        typeDetail.setId(record.getRecordId());
        typeDetail.setUserId(record.getUserId());
        typeDetail.setRecordTime(timeString);

        switch (record.getRecordType()) {
            case "agent" -> {
                RecordAgent recordAgent = recordAgentService.getById(record.getRecordId());
                typeDetail.setDosage(recordAgent.getDosage());
                typeDetail.setAgentName(recordAgent.getAgentName());
                typeDetail.setRemark(recordAgent.getRemark());
            }
            case "blood_sugar" -> {
                RecordBloodSugar recordBloodSugar = recordBloodSugarService.getById(record.getRecordId());
                typeDetail.setBloodSugarValue(recordBloodSugar.getBloodSugarValue());
                typeDetail.setMeasureTime(recordBloodSugar.getMeasureTime());
                typeDetail.setRemark(recordBloodSugar.getRemark());
            }
            case "diet" -> {
                RecordDiet recordDiet = recordDietService.getById(record.getRecordId());
                typeDetail.setFoodDetail(recordDiet.getFoodDetail());
                typeDetail.setFoodPic(recordDiet.getFoodPic());
                typeDetail.setMealType(recordDiet.getMealType());
                typeDetail.setTotalCarb(recordDiet.getTotalCarb());
                typeDetail.setTotalProtein(recordDiet.getTotalProtein());
                typeDetail.setTotalFat(recordDiet.getTotalFat());
                typeDetail.setTotalEnergy(recordDiet.getTotalEnergy());
                typeDetail.setRemark(recordDiet.getRemark());
            }
            case "exercise" -> {
                RecordExercise recordExercise = recordExerciseService.getById(record.getRecordId());
                typeDetail.setExerciseType(recordExercise.getExerciseType());
                typeDetail.setDuration(recordExercise.getDuration());
                typeDetail.setRemark(recordExercise.getRemark());
            }
            case "injection" -> {
                RecordInjection recordInjection = recordInjectionService.getById(record.getRecordId());
                typeDetail.setInsulinType(recordInjection.getInsulinType());
                typeDetail.setInjectionType(recordInjection.getInjectionType());
                typeDetail.setBolus(recordInjection.getBolus());
                typeDetail.setSquareWaveRate(recordInjection.getSquareWaveRate());
                typeDetail.setSquareWaveTime(recordInjection.getSquareWaveTime());
                typeDetail.setRemark(recordInjection.getRemark());
            }
        }

        vo.setTypeDetail(typeDetail);

        return R.success(vo);
    }

    private List<RecordVOForHomePage> recordToVO(List<Record> records){
        List<RecordVOForHomePage> recordList = new ArrayList<>();
        for (Record record : records) {
            switch (record.getRecordType()) {
                case "agent" -> {
                    RecordAgent recordAgent = recordAgentService.getById(record.getRecordId());
                    RecordVOForHomePage vo = agentToVO(recordAgent);
                    vo.setRecordRootId(record.getRecordRootId().toString());
                    recordList.add(vo);
                }
                case "blood_sugar" -> {
                    RecordBloodSugar recordBloodSugar = recordBloodSugarService.getById(record.getRecordId());
                    RecordVOForHomePage vo = bloodSugarToVO(recordBloodSugar);
                    vo.setRecordRootId(record.getRecordRootId().toString());
                    recordList.add(vo);
                }
                case "diet" -> {
                    RecordDiet recordDiet = recordDietService.getById(record.getRecordId());
                    RecordVOForHomePage vo = dietToVO(recordDiet);
                    vo.setRecordRootId(record.getRecordRootId().toString());
                    recordList.add(vo);
                }
                case "exercise" -> {
                    RecordExercise recordExercise = recordExerciseService.getById(record.getRecordId());
                    RecordVOForHomePage vo = exerciseToVO(recordExercise);
                    vo.setRecordRootId(record.getRecordRootId().toString());
                    recordList.add(vo);
                }
                case "injection" -> {
                    RecordInjection recordInjection = recordInjectionService.getById(record.getRecordId());
                    RecordVOForHomePage vo = injectionToVO(recordInjection);
                    vo.setRecordRootId(record.getRecordRootId().toString());
                    recordList.add(vo);
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
