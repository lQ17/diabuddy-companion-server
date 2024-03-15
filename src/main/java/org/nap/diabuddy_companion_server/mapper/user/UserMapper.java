package org.nap.diabuddy_companion_server.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.nap.diabuddy_companion_server.entity.user.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
