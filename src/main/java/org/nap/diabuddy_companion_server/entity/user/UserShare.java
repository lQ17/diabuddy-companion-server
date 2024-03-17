package org.nap.diabuddy_companion_server.entity.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserShare implements Serializable {
    private Integer id;
    private Integer userId;
    private Integer friendId;
    private String status;
}
