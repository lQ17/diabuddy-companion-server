package org.nap.diabuddy_companion_server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("user_favorite_foods")
public class Favorite implements Serializable {
    private Integer userId;
    private Integer foodId;
}
