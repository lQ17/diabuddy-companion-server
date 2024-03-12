package org.nap.diabuddy_companion_server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nap.diabuddy_companion_server.entity.User;
import org.nap.diabuddy_companion_server.mapper.UserMapper;
import org.nap.diabuddy_companion_server.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
