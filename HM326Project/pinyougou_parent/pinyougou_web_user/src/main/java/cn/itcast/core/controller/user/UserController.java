package cn.itcast.core.controller.user;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.user.UserService;
import cn.itcast.core.utils.phone.PhoneFormatCheckUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @Reference
    private UserService userService;

    @RequestMapping("add")
    public Result add(@RequestBody User user,String smscode){
        if (userService.checkSmsCode(user.getPhone(),smscode)==false){
            return new Result(false,"验证码不正确");
        }

        try {
            userService.add(user);
            return new Result(true, "注册成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"注册失败");
        }
    }

    @RequestMapping("sendCode")
    public Result sendCode(String phone){
        if (!PhoneFormatCheckUtils.isPhoneLegal(phone)){
            return new Result(false,"手机号码格式不正确");
        }

        try {
            userService.createSmsCode(phone);
            return new Result(true,"发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"发送失败");
        }
    }
}
