package cn.itcast.core.service.user;

import cn.itcast.core.pojo.user.User;

public interface UserService {

    void add(User user);

    void createSmsCode(String phone);

    boolean checkSmsCode(String phone,String code);
}
