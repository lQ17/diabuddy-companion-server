package org.nap.diabuddy_companion_server.entity.VO;

import lombok.Data;

import java.util.Date;

@Data
public class UserVO{
    private Integer id;
    private String username;
    private String userPic;
    private Date accountCreationDate;
}
