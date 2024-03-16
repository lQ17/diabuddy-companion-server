package org.nap.diabuddy_companion_server.entity.plan;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InsulinPumpBasalRate {
    private Integer id;
    private Integer userId;
    private LocalDateTime startTime;
    private LocalDateTime endedTime;
    private Float rate_0000;
    private Float rate_0030;
    private Float rate_0100;
    private Float rate_0130;
    private Float rate_0200;
    private Float rate_0230;
    private Float rate_0300;
    private Float rate_0330;
    private Float rate_0400;
    private Float rate_0430;
    private Float rate_0500;
    private Float rate_0530;
    private Float rate_0600;
    private Float rate_0630;
    private Float rate_0700;
    private Float rate_0730;
    private Float rate_0800;
    private Float rate_0830;
    private Float rate_0900;
    private Float rate_0930;
    private Float rate_1000;
    private Float rate_1030;
    private Float rate_1100;
    private Float rate_1130;
    private Float rate_1200;
    private Float rate_1230;
    private Float rate_1300;
    private Float rate_1330;
    private Float rate_1400;
    private Float rate_1430;
    private Float rate_1500;
    private Float rate_1530;
    private Float rate_1600;
    private Float rate_1630;
    private Float rate_1700;
    private Float rate_1730;
    private Float rate_1800;
    private Float rate_1830;
    private Float rate_1900;
    private Float rate_1930;
    private Float rate_2000;
    private Float rate_2030;
    private Float rate_2100;
    private Float rate_2130;
    private Float rate_2200;
    private Float rate_2230;
    private Float rate_2300;
    private Float rate_2330;

    public float sumOfRates() {
        Float[] rates = {
                rate_0000, rate_0030, rate_0100, rate_0130, rate_0200, rate_0230, rate_0300, rate_0330,
                rate_0400, rate_0430, rate_0500, rate_0530, rate_0600, rate_0630, rate_0700, rate_0730,
                rate_0800, rate_0830, rate_0900, rate_0930, rate_1000, rate_1030, rate_1100, rate_1130,
                rate_1200, rate_1230, rate_1300, rate_1330, rate_1400, rate_1430, rate_1500, rate_1530,
                rate_1600, rate_1630, rate_1700, rate_1730, rate_1800, rate_1830, rate_1900, rate_1930,
                rate_2000, rate_2030, rate_2100, rate_2130, rate_2200, rate_2230, rate_2300, rate_2330
        };

        float sum = 0;
        for (Float rate : rates) {
            if (rate != null) {
                sum += rate;

            }
        }
        return sum;
    }
}
