package org.nap.diabuddy_companion_server.entity;

import lombok.Data;

@Data
public class Food {

    private Integer foodId;
    private String foodName;
    private String foodPic;
    private Integer isReviewed;
    private Integer isPublicFood;
    private Integer createUserId;
    private String foodCategory;
    private String foodTinyCategory;
    private Integer isPackedFood;
    private String brandName;
    private String glycemicIndex;
    private String glycemicLoad;
    private String ediblePart;
    private String water;
    private String energy;
    private String protein;
    private String fat;
    private String cholesterol;
    private String ash;
    private String carb;
    private String dietaryFiber;
    private String carotene;
    private String vitaminA;
    private String alphaTe;
    private String thiamin;
    private String riboflavin;
    private String niacin;
    private String vitaminC;
    private String calcium;
    private String phosphorus;
    private String potassium;
    private String sodium;
    private String magnesium;
    private String iron;
    private String zinc;
    private String selenium;
    private String copper;
    private String manganese;
    private String iodine;
    private String saturatedFattyAcids;
    private String monounsaturatedFattyAcids;
    private String polyunsaturatedFattyAcids;
    private String totalFattyAcids;

//    public Food(){}
//
//    public Food(Integer foodId, String foodName, String foodPic, String energy, String carb){
//        this.foodId = foodId;
//        this.foodName = foodName;
//        this.foodPic = foodPic;
//        this.energy = energy;
//        this.carb = carb;
//    }
}