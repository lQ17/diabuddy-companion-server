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
import org.springframework.transaction.annotation.Transactional;
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

    @GetMapping("/phone/{userId}")
    public R<Object> getUserPhone(@PathVariable("userId") Integer userId){
        User user = userService.getById(userId);

        if(user == null){
            return R.error("用户不存在");
        }

        String phone = user.getPhone();

        HashMap<Object, Object> res = new HashMap<>();

        res.put("phone",phone);

        return R.success(res);
    }

    @PostMapping("/phone")
    public R<Object> updateUserPhone(@RequestBody User user){
        userService.updateById(user);
        return R.success(null);
    }

    @GetMapping("/private/{userId}")
    public R<HashMap<String, Integer>> getPrivateStatus(@PathVariable("userId") Integer userId){
        User user = userService.getById(userId);
        HashMap<String, Integer> res = new HashMap<>();
        Integer isPrivateUser = user.getIsPrivateUser();
        if(isPrivateUser == null){
            isPrivateUser = 0;
        }
        res.put("isPrivateUser",isPrivateUser);
        return R.success(res);
    }

    @PostMapping("/private")
    public R<Object> updatePrivateStatus(@RequestBody Map<String, Integer> dto){
        Integer userId = dto.get("user_id");
        Integer isPrivateUser = dto.get("is_private_user");

        User user = userService.getById(userId);

        if(user == null){
            return R.error("用户获取错误");
        }

        user.setIsPrivateUser(isPrivateUser);
        userService.updateById(user);

        return R.success(null);
    }

    @PutMapping("/update-password")
    public R<Object> updatePassword(@RequestBody Map<String, Object> dto){
        Integer userId = (Integer) dto.get("id");
        String oldPassword = (String) dto.get("old_password");
        String newPassword = (String) dto.get("new_password");

        User user = userService.getById(userId);

        if(!user.getPassword().equals(oldPassword)){
            return R.error("旧密码不正确");
        }

        user.setPassword(newPassword);

        userService.updateById(user);

        return R.success(null);
    }

    @PutMapping("/username")
    public R<Object> updateUsername(@RequestBody Map<String, Object> dto){
        Integer userId = (Integer) dto.get("id");
        String username = (String) dto.get("username");

        User user = userService.getById(userId);

        if(user == null){
            return R.error("用户不存在");
        }

        user.setUsername(username);

        userService.updateById(user);

        return R.success(null);
    }

    @PutMapping("/update-info")
    public R<Object> updateUserInfo(@RequestBody User userDTO){
        User userById = userService.getById(userDTO.getId());

        if(userById == null){
            return R.error("更新失败，请重试");
        }

        userById.setUsername(userDTO.getUsername());
        userById.setFullName(userDTO.getFullName());
        userById.setGender(userDTO.getGender());
        userById.setBirthday(userDTO.getBirthday());
        userById.setAddress(userDTO.getAddress());

        boolean isUpdated = userService.updateById(userById);

        if(isUpdated){
            return R.success(null);
        }else{
            return R.error("更新失败，请重试");
        }
    }

    @PutMapping("/pic")
    public R<Object> updateUserAvatar(@RequestBody User userDTO){
        User userById = userService.getById(userDTO.getId());

        if(userById == null){
            return R.error("更新错误，请重试");
        }

        userById.setUserPic(userDTO.getUserPic());
        boolean isUpdated = userService.updateById(userById);

        if(isUpdated){
            return R.success(null);
        }else{
            return R.error("更新失败，请重试");
        }

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
