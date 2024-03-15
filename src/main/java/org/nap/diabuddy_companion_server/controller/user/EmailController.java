package org.nap.diabuddy_companion_server.controller.user;

import jakarta.annotation.Resource;
import org.nap.diabuddy_companion_server.common.R;
import org.nap.diabuddy_companion_server.service.user.EmailService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/email")
public class EmailController {
    public static final String EMAIL_SUBJECT = "糖友萌验证码";
    public static final String EMAIL_TEXT_A = "你的验证码是: ";
    public final String EMAIL_TEXT_B = "，请于5分钟内输入。如非本人操作，请联系 diabuddy_companion@outlook.com";

    @Resource
    private EmailService emailService;


    @GetMapping("/send")
    public R<Object> sendMail(@RequestParam("email") String email) {

        // 1. 生成一个code返回
        String code = emailService.storeCode(email);

        // 2. 拼接内容，在上面的常量处设置
        String text = EMAIL_TEXT_A + code + EMAIL_TEXT_B;

        // 3. 发邮件
        emailService.sendSimpleMessage(email, EMAIL_SUBJECT, text);

        Map<String, Object> response = new HashMap<>();
        response.put("successMessage", "发送成功");

        return R.success(response);
    }


}
