package cn.itcast.core.controller.pay;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.service.order.OrderService;
import cn.itcast.core.service.pay.WeixinPayService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.IdWorker;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("pay")
public class PayController {

    @Reference(timeout = 1000*60*6)
    private WeixinPayService weixinPayService;

    @Reference
    private OrderService orderService;

    @RequestMapping("createNative")
    public Map createNative(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        PayLog payLog = orderService.searchPayLogFromRedis(name);
        if (payLog!=null) {
            return weixinPayService.createNative(payLog.getOutTradeNo(), payLog.getTotalFee() + "");
        }else {
            return new HashMap();
        }
    }

    @RequestMapping("queryPayStatus")
    public Result queryPayStatus(String out_trade_no){
        Map map = weixinPayService.queryPayStatusWhile(out_trade_no);
        if (map==null){
            return new Result(false,"二维码超时");
        }else {
            if ("SUCCESS".equals(map.get("trade_state"))){
                orderService.updateOrderStatus(out_trade_no,(String) map.get("transaction_id"));
                return new Result(true, "支付成功");
            }else {
                return new Result(false,"支付失败");
            }
        }
    }
}
