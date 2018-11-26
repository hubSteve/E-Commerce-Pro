package cn.itcast.core.service.user;

import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.user.UserService;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;


import javax.annotation.Resource;
import javax.jms.Destination;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private JmsTemplate jmsTemplate;

    @Resource
    private Destination smscodeDestination;

    @Override
    public void add(User user) {
        user.setCreated(new Date());
        user.setUpdated(new Date());
        String password = DigestUtils.md5Hex(user.getPassword());
        user.setPassword(password);
        userDao.insertSelective(user);
    }

    @Override
    public void createSmsCode(String phone) {
        long code = (long)(Math.random() * 1000000);
        if (code<100000){
            code=code+100000;
        }
        System.out.println("验证码"+code);
        redisTemplate.boundValueOps("smscode_"+phone).set(code+"",5,TimeUnit.MINUTES);

        Map map=new HashMap();
        map.put("mobile",phone);
        map.put("smscode",code+"");

        jmsTemplate.convertAndSend(smscodeDestination,map);

    }

    @Override
    public boolean checkSmsCode(String phone, String code) {
        String smsCode = (String) redisTemplate.boundValueOps("smscode_" + phone).get();
        if (smsCode!=null && code.equals(smsCode)){
            return true;
        }else {
            return false;
        }
    }
}
