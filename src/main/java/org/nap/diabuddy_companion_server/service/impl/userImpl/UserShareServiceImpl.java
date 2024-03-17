package org.nap.diabuddy_companion_server.service.impl.userImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nap.diabuddy_companion_server.entity.user.UserShare;
import org.nap.diabuddy_companion_server.mapper.user.UserShareMapper;
import org.nap.diabuddy_companion_server.service.user.UserShareService;
import org.springframework.stereotype.Service;

@Service
public class UserShareServiceImpl extends ServiceImpl<UserShareMapper, UserShare> implements UserShareService {
}
