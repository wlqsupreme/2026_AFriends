package Afriends_v3.service;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.IClientProfile;

/**
 * 阿里云短信服务实现类
 */
@Service
public class SmsService {
    // 从配置文件读取阿里云AccessKey
    @Value("${aliyun.sms.access-key-id}")
    private String accessKeyId;

    @Value("${aliyun.sms.access-key-secret}")
    private String accessKeySecret;

    // 短信签名（需与阿里云控制台一致）
//    @Value("${aliyun.sms.sign-name}")
    private String signName="景程未来";

    // 验证码短信模板CODE（需与阿里云控制台一致）
    @Value("${aliyun.sms.template-code.verify}")
    private String verifyTemplateCode;

    /**
     * 发送验证码短信
     * @param phone 手机号
     * @param code 验证码
     * @return 是否发送成功
     */
    public boolean sendVerificationCode(String phone, String code) {
        // 调用阿里云短信API发送短信
        try {
            SendSmsResponse response = sendSms(
                    phone,
                    signName,
                    verifyTemplateCode,
                    "{\"code\":\"" + code + "\"}" // 模板参数（JSON格式）
            );
            System.out.println("手机号：" + signName + ",结果："+verifyTemplateCode+"，手机号：" + phone + ",结果："+code);
            // 阿里云返回"OK"表示发送成功（实际需根据业务处理其他状态）
            return "OK".equals(response.getCode());
        } catch (ClientException e) {
            // 打印异常信息（实际项目中建议用日志框架）
            System.err.println("短信发送失败：" + e.getErrMsg());
            return false;
        }
    }

    /**
     * 通用短信发送方法（底层调用阿里云API）
     * @param phone 手机号
     * @param signName 短信签名
     * @param templateCode 模板CODE
     * @param templateParam 模板参数（JSON格式）
     * @return 阿里云响应结果
     */
    private SendSmsResponse sendSms(String phone, String signName, String templateCode, String templateParam) throws ClientException {
        // 初始化阿里云客户端
        IClientProfile profile = DefaultProfile.getProfile(
                "cn-hangzhou", // 地域ID（短信服务固定为cn-hangzhou）
                accessKeyId,
                accessKeySecret
        );
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Dysmsapi", "dysmsapi.aliyuncs.com");
        IAcsClient acsClient = new DefaultAcsClient(profile);

        // 构建发送请求
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone); // 接收短信的手机号
        request.setSignName(signName); // 短信签名
        request.setTemplateCode(templateCode); // 模板CODE
        request.setTemplateParam(templateParam); // 模板参数

        // 发送请求并返回结果
        return acsClient.getAcsResponse(request);
    }
}
