package cn.itcast.core.service.user;

import cn.itcast.core.pojo.address.Address;

import java.util.List;

public interface AddressService {

    List<Address> findListByUserId(String userId);
}
