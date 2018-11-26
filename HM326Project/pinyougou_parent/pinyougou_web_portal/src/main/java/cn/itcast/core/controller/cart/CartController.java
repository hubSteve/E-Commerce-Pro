package cn.itcast.core.controller.cart;


import cn.itcast.core.entity.LoginResult;
import cn.itcast.core.pojo.cart.Cart;
import cn.itcast.core.service.cart.CartService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("cart")
public class CartController {

    @Reference
    private CartService cartService;

    @RequestMapping("addGoodsToCartList")
    public LoginResult addGoodsToCartList(@RequestBody List<Cart> cartList,Long itemId,Integer num){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(name);
        try {

            if ("anonymousUser".equals(name)){
                cartList = cartService.addGoodsToCartList(cartList, itemId, num);
                name="";
            }else{
                List<Cart> cartList_redis = cartService.findCartListFromRedis(name);
                cartList = cartService.addGoodsToCartList(cartList_redis, itemId, num);
                cartService.setCartListToRedis(name,cartList);
            }
            return new LoginResult(true,name,cartList);
        } catch (Exception e) {
            e.printStackTrace();
            return new LoginResult(false,name,"添加失败");
        }
    }

    @RequestMapping("findCartList")
    public LoginResult findCartList(@RequestBody List<Cart> cartList){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if ("anonymousUser".equals(name)){
            return new LoginResult(true,"",cartList);
        }else{
            List<Cart> carts = cartService.findCartListFromRedis(name);
            if (cartList.size()>0){
                carts = cartService.mergeCartList(cartList,carts);
                cartService.setCartListToRedis(name,carts);
            }
            return new LoginResult(true,name,carts);
        }
    }
}
