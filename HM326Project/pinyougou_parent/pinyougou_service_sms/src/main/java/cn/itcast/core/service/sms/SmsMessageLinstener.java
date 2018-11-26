package cn.itcast.core.service.sms;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

public class SmsMessageLinstener implements MessageListener {

    @Resource
    private SmsUtil smsUtil;

    @Value("${templateCode_smscode}")
    private String templateCode;

    @Value("${templateParam_smscode}")
    private String param;

    @Override
    public void onMessage(Message message) {
        MapMessage mapMessage=(MapMessage)message;

        try {
            String mobile = mapMessage.getString("mobile");
            String smscode = mapMessage.getString("smscode");
            System.out.println("接收到短信"+mobile+"..."+smscode);

            smsUtil.sendSms(mobile,templateCode,param.replace("[value]",smscode));
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
