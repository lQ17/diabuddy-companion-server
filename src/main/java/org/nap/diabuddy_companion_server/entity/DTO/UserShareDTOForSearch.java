package org.nap.diabuddy_companion_server.entity.DTO;

import lombok.Data;

@Data
public class UserShareDTOForSearch {
    private Integer userId;
    private Integer friendId;
    private String status;
}
