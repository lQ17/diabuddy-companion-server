package org.nap.diabuddy_companion_server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("blood_sugar_records")
public class RecordBloodSugar implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Float bloodSugarValue;

    private String measureTime;

    private Date recordTime;

    private String remark;

    // 枚举类型的处理
//    public enum MeasureTime {
//        EARLY_MORNING,
//        FASTING,
//        AFTER_BREAKFAST,
//        BEFORE_LUNCH,
//        AFTER_LUNCH,
//        BEFORE_DINNER,
//        AFTER_DINNER,
//        BEFORE_BED,
//        RANDOM;
//    }


//    public MeasureTime getMeasureTimeEnum() {
//        return MeasureTime.valueOf(measureTime.toUpperCase());
//    }
//
//    public void setMeasureTimeEnum(MeasureTime measureTime) {
//        this.measureTime = measureTime.name().toLowerCase();
//    }
}
