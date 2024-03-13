package org.nap.diabuddy_companion_server.entity.VO;

import lombok.Data;

@Data
public class FoodVOForList{

    private Integer foodId;
    private String foodName;
    private String foodPic;
    private String energy;
    private String carb;
}

