package org.nap.diabuddy_companion_server.entity;


import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class User implements Serializable {

    private Integer id;

    private String username;

    private String password;

    private String email;

    private String userPic;

    private String fullName;

    private Date birthday;

    private String gender;

    private String phone;

    private String emergencyContact;

    private String address;

    private Date lastLogin;

    private Date accountCreationDate;

    private Integer accountStatus;

    // 控糖计划
    private Float tdd;
    private Float icr;
    private Float isf;
    private Float dayEatingEnergy;
    private Float dayEatingCarb;
    private Float dayEatingProtein;
    private Float dayEatingFat;

    // 默认构造方法
    public User() {
    }

    // 特殊构造方法
    public User(Integer id, String username, String userPic,Date accountCreationDate) {
        this.id = id;
        this.username = username;
        this.userPic = userPic;
        this.accountCreationDate = accountCreationDate;
    }


}
