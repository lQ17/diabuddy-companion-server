package org.nap.diabuddy_companion_server.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.nap.diabuddy_companion_server.common.JWTUtils;
import org.nap.diabuddy_companion_server.common.R;
import org.nap.diabuddy_companion_server.entity.user.User;
import org.nap.diabuddy_companion_server.entity.VO.UserVO;
import org.nap.diabuddy_companion_server.service.user.EmailService;
import org.nap.diabuddy_companion_server.service.user.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private EmailService emailService;

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

    @PostMapping("/register-by-email")
    public R<Object> regByEmail(@RequestBody Map<String, Object> payload){

        String email = (String) payload.get("email");
        String password = (String) payload.get("password");
        String repassword = (String) payload.get("repassword");
        String emailMsg = (String) payload.get("emailMsg");

        boolean validateSuccess = emailService.validateCode(email, emailMsg);

        if(!password.equals(repassword)){
            R.error("两次密码不一致");
        }

        if(!validateSuccess){
            R.error("验证码错误！");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setAccountCreationDate(new Date());
        user.setAccountStatus(1);

        // 生成随机用户名
        String baseUsername = email.split("@")[0]; // 获取邮箱前缀
        String randomSuffix = generateRandomString(5); // 生成5位随机小写字母
        user.setUsername(baseUsername + randomSuffix);


        userService.save(user);

        return R.success(null);
    }

    private String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        while (length > 0) {
            result.append(characters.charAt(random.nextInt(characters.length())));
            length--;
        }
        return result.toString();
    }
}
