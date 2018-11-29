package cn.itcast.core.service.user;


import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.user.UserDao;

import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderQuery;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;


import com.github.pagehelper.PageInfo;
import org.apache.commons.codec.digest.DigestUtils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;


import javax.annotation.Resource;
import javax.jms.Destination;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private OrderDao orderDao;

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


    /** 运营商后台查询用户信息分页查询
     * @param pageNum
     * @param pageSize
     * @param user
     * @return
     */
    @Override
    public PageInfo<User> search(int pageNum, int pageSize, User user) {

        PageHelper.startPage(pageNum, pageSize);
        UserQuery userQuery = new UserQuery();
        UserQuery.Criteria criteria = userQuery.createCriteria();
        //封装查询条件
        if (user.getUsername()!=null&& !"".equals(user.getUsername())){

            criteria.andUsernameLike("%"+user.getUsername()+"%");
        }
        if (user.getStatus()!=null&& !"".equals(user.getStatus())){

            criteria.andStatusLike ("%"+user.getStatus()+"%");
        }
        if (user.getNickName()!=null&& !"".equals(user.getNickName ())){

            criteria.andNickNameLike ("%"+user.getNickName ()+"%");
        }


        List<User> userList = userDao.selectByExample(userQuery);

        return  new PageInfo<>(userList);
    }


    /**运营商后台更新修改用户回显
     * @param id
     * @return
     */
    @Override
    public User findOne(long id) {
        return userDao.selectByPrimaryKey(id);
    }


    /** 运营商修改状态
     * @param user
     */
    @Override
    public void update(User user) {

        User user1 = new User();
        if (user.getId()!=null&&!"".equals(user.getId())){

            user1 = userDao.selectByPrimaryKey(user.getId());
        }else {
            throw new RuntimeException();
        }

        if (user.getStatus()!=null&&!"".equals(user.getStatus())){

            user1.setStatus(user.getStatus());
        }else {
            throw new RuntimeException();
        }
        userDao.updateByPrimaryKeySelective(user1);
    }

    /** 运营商删除用户
     * @param ids
     */
    @Override
    public void delete(long[] ids) {
        if (ids!=null&&ids.length>0){
            for (long id : ids) {
                userDao.deleteByPrimaryKey(id);
            }
        }
    }

    @Override
    public List<Order> findCartList(String userId) {
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.createCriteria().andUserIdEqualTo(userId);
        return orderDao.selectByExample(orderQuery);
    }

    @Override
    public PageInfo<Order> abfindCartList(Integer Page, Integer pageSize, String username) {
        //获取当前用户id
        UserQuery userQuery = new UserQuery();
        userQuery.createCriteria().andUsernameEqualTo(username);
        List<User> users = userDao.selectByExample(userQuery);
        Long orderId =null;
        for (User user : users) {
            orderId = user.getId();
        }


        //根据id 查询订单
        PageHelper.startPage(Page,pageSize );
        OrderQuery orderQuery = new OrderQuery();
        OrderQuery.Criteria criteria = orderQuery.createCriteria();
        if (orderId!=null &&!"".equals(orderId))
        //封装检索条件
        {
            criteria.andOrderIdEqualTo(orderId);

        }
        List<Order> orders = orderDao.selectByExample(orderQuery);

        PageInfo<Order> pageInfo =new PageInfo<>(orders);
        return pageInfo;
    }
}
