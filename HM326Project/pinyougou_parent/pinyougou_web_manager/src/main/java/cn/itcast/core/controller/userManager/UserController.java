package cn.itcast.core.controller.userManager;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;
import cn.itcast.core.service.user.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userManager")
public class UserController {


    @Reference
    private UserService userService;



    @RequestMapping("/search.do")
    public PageInfo<User> search(int pageNum , int pageSize, @RequestBody User user){


        return  userService.search(pageNum,pageSize,user);
    }


    /** 修改用户状态回显数据
     * @param id
     * @return
     */
    @RequestMapping("/findOne.do")
    public User findOne(long id){

        System.out.println("翻山倒海发快递几号放假"+userService.findOne(id));
        return userService.findOne(id);
    }


    @RequestMapping("/update.do")
    public Result update(@RequestBody User user){

        try {
            userService.update(user);
            return new Result(true,"更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"更新失败");

        }
    }


    @RequestMapping("/delete.do")
    public Result delete(long[] ids){

        try {
            userService.delete(ids);
            return new Result(true,"删除成功");
        } catch (Exception e) {

            e.printStackTrace();
            return new Result(false,"删除失败");

        }
    }
}
