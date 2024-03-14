package org.nap.diabuddy_companion_server.entity.VO;

import lombok.Data;

@Data
public class RecordVOForHomePage {

    private String recordId;

    private String recordType;

    private String mainKey;

    private String mainValue;

    public RecordVOForHomePage() {
    }

    public RecordVOForHomePage(String recordId, String recordType, String mainKey, String mainValue) {
        this.recordId = recordId;
        this.recordType = recordType;
        this.mainKey = mainKey;
        this.mainValue = mainValue;
    }
}
