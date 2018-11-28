package cn.itcast.core.controller.user;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.user.UserService;
import cn.itcast.core.utils.phone.PhoneFormatCheckUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sun.rmi.server.InactiveGroupException;

import java.util.List;

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

    @RequestMapping("/findCartList.do")
    @ResponseBody
    public List<Order> findCartList() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("---------------------------------------------go in this method ,go on.");
        //System.out.println( "的发生地共发生京东方法按时发"+userService.findCartList(userId));
        //List<Order> list =userService.findCartList(userId);
        //System.out.println(list);
        return userService.findCartList(userId);
        // return null;
    }
    @RequestMapping("pageInfoOrdeList")
    public PageInfo<Order> pageInfoOrdeList(Integer page ,Integer pageSize){


        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username!=null){
            PageInfo<Order> pageInfo = userService.abfindCartList(page, pageSize, username);
            return pageInfo;

        }
        return null;

    }



}
