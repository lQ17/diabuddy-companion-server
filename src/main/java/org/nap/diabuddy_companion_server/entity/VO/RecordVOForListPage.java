package org.nap.diabuddy_companion_server.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
public class RecordVOForListPage {

    private Integer recordRootId;

    private String recordType;

    private Date recordTime;

    //用药相关
    private String dosage;
    private String agentName;

    //血糖相关
    private String bloodSugarLevel;
    private String measureTime;

    //饮食相关
    private String totalCarb;
    private String mealType;

    //运动相关
    private String duration;
    private String exerciseType;

    //注射相关
    private String injectionType;
    private String bolus;
    private String squareWaveRate;
    private String squareWaveTime;

    public RecordVOForListPage(Integer recordRootId, String recordType, Date recordTime) {
        this.recordRootId = recordRootId;
        this.recordType = recordType;
        this.recordTime = recordTime;
    }
}
