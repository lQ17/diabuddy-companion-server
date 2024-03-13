package org.nap.diabuddy_companion_server.entity.VO;

import lombok.Data;

@Data
public class FoodVOForDetail {
    private Integer foodId;
    private String foodName;
    private String foodPic;
    private String energy;
    private String carb;
    private String fat;
    private String protein;
    private String glycemicIndex;
    private String glycemicLoad;
    private String dietaryFiber;
    private String sodium;
}
