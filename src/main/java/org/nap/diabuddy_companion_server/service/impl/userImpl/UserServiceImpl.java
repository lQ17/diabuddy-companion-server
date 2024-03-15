package org.nap.diabuddy_companion_server.service.impl.userImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nap.diabuddy_companion_server.entity.user.User;
import org.nap.diabuddy_companion_server.mapper.user.UserMapper;
import org.nap.diabuddy_companion_server.service.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
