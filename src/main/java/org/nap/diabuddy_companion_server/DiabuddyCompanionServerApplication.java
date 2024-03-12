package org.nap.diabuddy_companion_server;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@MapperScan(basePackages = "org.nap.diabuddy_companion_server.mapper")
public class DiabuddyCompanionServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiabuddyCompanionServerApplication.class, args);
        log.info("项目启动成功>>>>>");
    }

}
