package org.nap.diabuddy_companion_server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nap.diabuddy_companion_server.entity.Favorite;
import org.nap.diabuddy_companion_server.mapper.FavoriteMapper;
import org.nap.diabuddy_companion_server.service.FavoriteService;
import org.springframework.stereotype.Service;

@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {
}
