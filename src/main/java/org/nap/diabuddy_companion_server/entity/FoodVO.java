package org.nap.diabuddy_companion_server.entity;

import lombok.Data;

@Data
public class FoodVO{

    private Integer foodId;
    private String foodName;
    private String foodPic;
    private String energy;
    private String carb;
}
