package cn.itcast.core.service.seller;

import cn.itcast.core.pojo.seller.Seller;
import com.github.pagehelper.PageInfo;

public interface SellerService {

    void add(Seller seller);

    PageInfo<Seller> search(Integer page, Integer rows, Seller seller);

    Seller findOne(String id);

    void updateStatus(String sellerId, String status);
}
