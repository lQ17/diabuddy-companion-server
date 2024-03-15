package org.nap.diabuddy_companion_server.entity.VO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class RecordVOForDetail {

    private String recordType;
    private String recordTime;
    private TypeDetail typeDetail;

    @Getter
    @Setter
    public static class TypeDetail {
        private Integer id;
        private Integer userId;
        private Integer dosage;
        private String agentName;
        private String recordTime;
        private String remark;
        private Float bloodSugarValue;
        private String measureTime;
        private String foodDetail;
        private String foodPic;
        private String mealType;
        private Float totalCarb;
        private Float totalEnergy;
        private Float totalFat;
        private Float totalProtein;
        private String exerciseType;
        private Integer duration;
        private String injectionType;
        private String insulinType;
        private Float bolus;
        private Float squareWaveRate;
        private Integer squareWaveTime;

    }

}
