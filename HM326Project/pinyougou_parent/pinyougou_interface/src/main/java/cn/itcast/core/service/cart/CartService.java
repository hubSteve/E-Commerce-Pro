package cn.itcast.core.service.cart;

import cn.itcast.core.pojo.cart.Cart;

import java.util.List;

public interface CartService {

    List<Cart> addGoodsToCartList(List<Cart> cartList,Long itemId,Integer num);

    void setCartListToRedis(String username,List<Cart> cartList);

    List<Cart> findCartListFromRedis(String username);

    List<Cart> mergeCartList(List<Cart> cartList1,List<Cart> cartList2);
}
