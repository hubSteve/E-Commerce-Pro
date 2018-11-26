package cn.itcast.core.controller.pay;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.service.pay.WeixinPayService;
import cn.itcast.core.service.seckill.SeckillOrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("pay")
public class PayController {

    @Reference
    private SeckillOrderService seckillOrderService;

    @Reference
    private WeixinPayService weixinPayService;


    @RequestMapping("createNative")
    public Map createNative(){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        SeckillOrder seckillOrder = seckillOrderService.searchOrderFromRedis(userId);
        if (seckillOrder!=null){
            long fen = (long)(seckillOrder.getMoney().doubleValue()*100);
           return weixinPayService.createNative(seckillOrder.getId()+"",fen+"");
        }else {
            return new HashMap();
        }
    }

    @RequestMapping("queryPayStatus")
    public Result queryPayStatus(String out_trade_no) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Map map = weixinPayService.queryPayStatusWhile(out_trade_no);
        if (map == null) {
            return new Result(false, "系统出错");
        } else {
            if ("SUCCESS".equals(map.get("trade_state"))) {
                try {
                    seckillOrderService.saveOrderFromRedis(userId, Long.valueOf(out_trade_no), (String) map.get("transaction_id"));
                    return new Result(true, "支付成功");
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return new Result(false, e.getMessage());
                }catch (Exception e){
                    e.printStackTrace();
                    return new Result(false, "支付失败");
                }
            }else {
                seckillOrderService.deleteOrderFromRedis(userId,Long.valueOf(out_trade_no));
                return new Result(false, "二维码超时");
            }
        }
    }
}
