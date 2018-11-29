package cn.itcast.core.controller.user;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.user.UserAddressService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//个人信息注册
@RestController
@RequestMapping("/userAddress")
public class UserAddressController {

    @Reference
    private UserAddressService userAddressService;

    @RequestMapping("/update")
    public Result update(@RequestBody User user){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        user.setUsername(name);
            try {
                userAddressService.update(user);
                return new Result(true,"添加成功");
            } catch (Exception e) {

                e.printStackTrace();
                return new Result(false,"添加失败");
            }


    }

}
