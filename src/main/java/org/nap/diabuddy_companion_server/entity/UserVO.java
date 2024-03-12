package org.nap.diabuddy_companion_server.entity;

import lombok.Data;

import java.time.temporal.ChronoUnit;
import java.util.Date;

@Data
public class UserVO extends User{

    public UserVO() {
        super();
    }
    // 特殊构造方法
    public UserVO(Integer id, String username, String userPic,Date accountCreationDate) {
        super(id, username, userPic, accountCreationDate);
    }
}
