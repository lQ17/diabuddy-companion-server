package org.nap.diabuddy_companion_server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("diet_records")
public class RecordDiet implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private String mealType;

    private Date recordTime;

    private String foodDetail;

    private String foodPic;

    private Float totalEnergy;

    private Float totalCarb;

    private Float totalFat;

    private Float totalProtein;

    private String remark;

//    public enum MealType {
//        BREAKFAST,
//        LUNCH,
//        DINNER,
//        SNACK;
//
//    }

//    public MealType getMealTypeEnum() {
//        return MealType.valueOf(mealType.toUpperCase());
//    }
//
//    public void setMealTypeEnum(MealType mealType) {
//        this.mealType = mealType.name().toLowerCase();
//    }
}
