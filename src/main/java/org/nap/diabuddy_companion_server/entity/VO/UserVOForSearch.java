package org.nap.diabuddy_companion_server.entity.VO;

import lombok.Data;

import java.util.Date;

@Data
public class UserVOForSearch {
    private Integer id; // userId
    private String username;
    private String fullName;
    private String phone;
    private Date birthday;
}
