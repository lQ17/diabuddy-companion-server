package org.nap.diabuddy_companion_server.entity.DTO;

import lombok.Data;

@Data
public class PlanDTO {
    private Integer userId;
    private Float tdd;
    private Float icr;
    private Float isf;
    private Float dayEatingEnergy;
    private Float dayEatingCarb;
    private Float dayEatingProtein;
    private Float dayEatingFat;
}
