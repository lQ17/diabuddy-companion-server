package org.nap.diabuddy_companion_server.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.nap.diabuddy_companion_server.common.JWTUtils;
import org.nap.diabuddy_companion_server.common.R;
import org.nap.diabuddy_companion_server.entity.User;
import org.nap.diabuddy_companion_server.entity.UserVO;
import org.nap.diabuddy_companion_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login-by-email-password")
    public R<Object> loginByEmailPassword(HttpServletRequest request, @RequestBody User user) {

        //根据邮箱查数据库
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        //构造条件
        queryWrapper.eq(User::getEmail, user.getEmail());

        return verifyAndSend(request, queryWrapper, user);

    }

    @PostMapping("/login-by-phone-password")
    public R<Object> loginByPhonePassword(HttpServletRequest request, @RequestBody User user) {

        //根据手机号查数据库
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        //构造条件
        queryWrapper.eq(User::getPhone, user.getPhone());

        return verifyAndSend(request, queryWrapper, user);
    }

    private R<Object> verifyAndSend (HttpServletRequest request, LambdaQueryWrapper<User> queryWrapper, User user) {
        //查到的用户实体
        User userFromDB = userService.getOne(queryWrapper);

        //如果没查到该用户
        if(userFromDB == null){
            return R.error("账号或密码错误");
        }

        //密码比对
        if(!userFromDB.getPassword().equals(user.getPassword())){
            return R.error("账号或密码错误");
        }

        //账户是否禁用
        if(userFromDB.getAccountStatus() == 0){
            return R.error("账户已禁用");
        }

        //发一个Token
        Map<String, String> map = new HashMap<>();//用来存放payload
        map.put("id",userFromDB.getId().toString());
        map.put("username",userFromDB.getUsername());
        String token = "Bearer " + JWTUtils.getToken(map);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", userFromDB.getId());

        return R.success(response);
    }

    @GetMapping("/info")
    public R<UserVO> getInfo (@RequestParam("userId") Integer userId){
        User user = userService.getById(userId);

        // 自动生成VO
        ModelMapper modelMapper = new ModelMapper();
        UserVO userVO = modelMapper.map(user, UserVO.class);

        return R.success(userVO);
    }


}
