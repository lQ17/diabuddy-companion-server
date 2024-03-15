package org.nap.diabuddy_companion_server.service.impl.foodImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nap.diabuddy_companion_server.entity.food.Favorite;
import org.nap.diabuddy_companion_server.mapper.food.FavoriteMapper;
import org.nap.diabuddy_companion_server.service.food.FavoriteService;
import org.springframework.stereotype.Service;

@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {
}
