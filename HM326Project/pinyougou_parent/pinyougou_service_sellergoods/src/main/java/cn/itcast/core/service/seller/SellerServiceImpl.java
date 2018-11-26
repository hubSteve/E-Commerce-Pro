package cn.itcast.core.service.seller;

import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.pojo.seller.SellerQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SellerServiceImpl implements SellerService {

    @Resource
    private SellerDao sellerDao;

    @Override
    public void add(Seller seller) {
        seller.setStatus("0");
        seller.setCreateTime(new Date());
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode(seller.getPassword());
        seller.setPassword(password);
        sellerDao.insertSelective(seller);
    }

    @Override
    public PageInfo<Seller> search(Integer page, Integer rows, Seller seller) {
        PageHelper.startPage(page,rows);
        SellerQuery sellerQuery=new SellerQuery();
        SellerQuery.Criteria criteria = sellerQuery.createCriteria();
        if (seller.getName()!=null && !"".equals(seller.getName().trim())){
            criteria.andNameLike("%"+seller.getName().trim()+"%");
        }
        if (seller.getNickName()!=null && !"".equals(seller.getNickName().trim())){
            criteria.andNickNameLike("%"+seller.getNickName().trim()+"%");
        }
        if (seller.getStatus()!=null && !"".equals(seller.getStatus().trim())){
            criteria.andStatusEqualTo(seller.getStatus().trim());
        }
        List<Seller> sellerList = sellerDao.selectByExample(sellerQuery);
        PageInfo<Seller> pageInfo=new PageInfo<>(sellerList);
        return pageInfo;
    }

    @Override
    public Seller findOne(String id) {
        return sellerDao.selectByPrimaryKey(id);
    }

    @Override
    public void updateStatus(String sellerId, String status) {
        Seller seller=new Seller();
        seller.setSellerId(sellerId);
        seller.setStatus(status);
        sellerDao.updateByPrimaryKeySelective(seller);
    }
}
