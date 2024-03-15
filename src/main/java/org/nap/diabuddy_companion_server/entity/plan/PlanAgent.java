package org.nap.diabuddy_companion_server.entity.plan;

import lombok.Data;

@Data
public class PlanAgent {
    private Integer id;
    private Integer planId;
    private String agentName;
    private Float agentDosage;
}
