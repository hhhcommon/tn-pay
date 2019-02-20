package com.tn.pay.manager.baofoo;

import com.tn.pay.dto.BindCardDTO;
import com.tn.pay.module.BindCardInfo;
import com.tn.pay.module.PaymentChannel;
import com.tn.pay.rsa.RsaCodingUtil;
import com.tn.pay.rsa.SignatureUtils;
import com.tn.pay.utils.DateUtil;
import com.tn.pay.utils.SecurityUtil;
import com.tn.pay.utils.StringUtil;
import com.tn.pay.utils.UrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.*;

/**
 * 绑卡处理类
 * 1 组装参数
 * 2 处理结果
 */
@Slf4j
@Component
public class BindCardManager extends BindManager {

    //短信验证码失效||短信验证码已过期，请重新发送||短信验证码错误次数超限，请重新获取
    public static final List<String> SMS_INVALID = new ArrayList<>(Arrays.asList("BF00106", "BF00260", "BF00261"));

    //对称密钥（可随机生成  商户自定义(AES key长度为=16位)）
    private String aes_key;
    //明文01|对称密钥，01代表AES[密码商户自定义]
    private String dgtl_envlp;

    public void setAesKey(String aesKey) {
        Assert.isTrue(aesKey.length() == 16, "密钥长度为16位！");
        this.aes_key = aesKey;
        this.dgtl_envlp = "01|" + aesKey;
    }

    @Override
    protected Map<String, String> readySignParam(PaymentChannel channel, BindCardDTO bindCardDTO) throws Exception {
        String pubKeyPath = channel.getPubKeyPath();
        String priKeyPath = channel.getPriKeyPath();
        String priKeyPassword = channel.getPriKeyPassword();
        String version = channel.getVersion();
        String memberNo = channel.getMemberNo();
        String terminalNo = channel.getTerminalNo();
        String bankAccount = bindCardDTO.getBankAccount();
        String userName = bindCardDTO.getUserName();
        String certNo = bindCardDTO.getCertNo();
        String mobile = bindCardDTO.getMobile();
        String bankSecurityCode = bindCardDTO.getBankSecurityCode() != null ? bindCardDTO.getBankSecurityCode() : "";
        String bankValidThru = bindCardDTO.getBankValidThru() != null ? bindCardDTO.getBankValidThru() : "";
        String userId = bindCardDTO.getUserId();
        Integer bankCardType = bindCardDTO.getBankCardType();
        String certType = bindCardDTO.getCertType();

        //账户信息[银行卡号|持卡人姓名|证件号|手机号|银行卡安全码|银行卡有效期]
        String cardInfo = bankAccount + "|" + userName + "|" + certNo + "|" + mobile + "|" + bankSecurityCode + "|" + bankValidThru;
        log.info("预绑卡-卡号:{},cardInfo:{}", bankAccount, cardInfo);

        Map<String, String> dataArray = new TreeMap<>();
        dataArray.put("send_time", DateUtil.getNowTime());
        dataArray.put("txn_type", "01");//交易类型
        dataArray.put("msg_id", StringUtil.getUUID());
        dataArray.put("version", version);
        dataArray.put("terminal_id", terminalNo);
        dataArray.put("member_id", memberNo);
        dataArray.put("dgtl_envlp", getDgtlEnvlp(pubKeyPath));
        dataArray.put("user_id", userId);//用户在商户平台唯一ID
        dataArray.put("card_type", bankCardType.toString());//卡类型  101	借记卡，102 信用卡
        dataArray.put("id_card_type", certType);//证件类型
        dataArray.put("acc_info", encryptAES(cardInfo));

        String sign = sign(dataArray, priKeyPath, priKeyPassword);
        dataArray.put("signature", sign);//签名域
        return dataArray;
    }

    @Override
    protected String readySignProcess(PaymentChannel channel, Map<String, String> returnData) throws Exception {
        String priKeyPath = channel.getPriKeyPath();
        String priKeyPassword = channel.getPriKeyPassword();
        String resp_code = returnData.get("resp_code");
        String biz_resp_code = returnData.get("biz_resp_code");
        String biz_resp_msg = returnData.get("biz_resp_msg");
        if ("S".equals(resp_code)) {
            if (!returnData.containsKey("dgtl_envlp")) {
                throw new Exception("缺少dgtl_envlp参数！");
            }
            String dgtl_envlp = RsaCodingUtil.decryptByPriPfxFile(returnData.get("dgtl_envlp"), priKeyPath, priKeyPassword);
            dgtl_envlp = SecurityUtil.Base64Decode(dgtl_envlp);
            String aes_key = StringUtil.getAesKey(dgtl_envlp);//获取返回的AES_KEY
            return SecurityUtil.Base64Decode(SecurityUtil.AesDecrypt(returnData.get("unique_code"), aes_key));
        } else {
            throw new Exception("预绑卡失败！[" + biz_resp_code + "]" + biz_resp_msg);
        }
    }

    @Override
    protected Map<String, String> confirmSignParam(PaymentChannel channel, String uniqueCode) throws Exception {
        String pubKeyPath = channel.getPubKeyPath();
        String priKeyPath = channel.getPriKeyPath();
        String priKeyPassword = channel.getPriKeyPassword();
        String version = channel.getVersion();
        String memberNo = channel.getMemberNo();
        String terminalNo = channel.getTerminalNo();

        Map<String, String> dataArray = new TreeMap<>();
        dataArray.put("send_time", DateUtil.getNowTime());
        dataArray.put("txn_type", "02");//交易类型
        dataArray.put("msg_id", StringUtil.getUUID());
        dataArray.put("version", version);
        dataArray.put("terminal_id", terminalNo);
        dataArray.put("member_id", memberNo);
        dataArray.put("dgtl_envlp", getDgtlEnvlp(pubKeyPath));
        dataArray.put("unique_code", encryptAES(uniqueCode));//预签约唯一码

        String sign = sign(dataArray, priKeyPath, priKeyPassword);
        dataArray.put("signature", sign);//签名域
        return dataArray;
    }

    @Override
    protected String confirmSignProcess(PaymentChannel channel, Map<String, String> returnData, BindCardInfo bindCardInfo) throws Exception {
        String priKeyPath = channel.getPriKeyPath();
        String priKeyPassword = channel.getPriKeyPassword();
        String resp_code = returnData.get("resp_code");
        String biz_resp_code = returnData.get("biz_resp_code");
        String biz_resp_msg = returnData.get("biz_resp_msg");
        if ("S".equals(resp_code)) {
            if (!returnData.containsKey("dgtl_envlp")) {
                throw new Exception("缺少dgtl_envlp参数！");
            }
            String dgtl_envlp = RsaCodingUtil.decryptByPriPfxFile(returnData.get("dgtl_envlp"), priKeyPath, priKeyPassword);
            dgtl_envlp = SecurityUtil.Base64Decode(dgtl_envlp);
            String aes_key = StringUtil.getAesKey(dgtl_envlp);//获取返回的AES_KEY
            return SecurityUtil.Base64Decode(SecurityUtil.AesDecrypt(returnData.get("protocol_no"), aes_key));
        } else if ("I".equals(resp_code)) {
            // throw new Exception("确认绑卡处理中！["+biz_resp_code+"]"+biz_resp_msg);
            // 处理中，转状态为3，定时查询绑卡状态
            return null;
        } else if ("F".equals(resp_code)) {
            // 短信失效删除历史
            if (SMS_INVALID.contains(biz_resp_code)) {
                bindCardInfo.deleteById();
            }
            throw new Exception("确认绑卡失败！[" + biz_resp_code + "]" + biz_resp_msg);
        } else {
            throw new Exception("确认绑卡宝付异常！");//异常不得做为订单状态。
        }
    }

    @Override
    protected Map<String, String> queryBindParam(PaymentChannel channel, BindCardDTO bindCardDTO) throws Exception {
        String pubKeyPath = channel.getPubKeyPath();
        String priKeyPath = channel.getPriKeyPath();
        String priKeyPassword = channel.getPriKeyPassword();
        String version = channel.getVersion();
        String memberNo = channel.getMemberNo();
        String terminalNo = channel.getTerminalNo();
        String bankAccount = bindCardDTO.getBankAccount();
        String userId = bindCardDTO.getUserId();

        Map<String, String> dataArray = new TreeMap<>();
        dataArray.put("send_time", DateUtil.getNowTime());
        dataArray.put("txn_type", "03");//交易类型
        dataArray.put("msg_id", StringUtil.getUUID());
        dataArray.put("version", version);
        dataArray.put("terminal_id", terminalNo);
        dataArray.put("member_id", memberNo);
        dataArray.put("dgtl_envlp", getDgtlEnvlp(pubKeyPath));
        dataArray.put("user_id", userId);//用户在平台的唯一ID
        dataArray.put("acc_no", encryptAES(bankAccount));//银行卡号密文[与user_id必须其中一个有值]

        String sign = sign(dataArray, priKeyPath, priKeyPassword);
        dataArray.put("signature", sign);//签名域
        return dataArray;
    }

    @Override
    protected String queryBindProcess(PaymentChannel channel, Map<String, String> returnData) throws Exception {
        String priKeyPath = channel.getPriKeyPath();
        String priKeyPassword = channel.getPriKeyPassword();
        String resp_code = returnData.get("resp_code");
        String biz_resp_code = returnData.get("biz_resp_code");
        String biz_resp_msg = returnData.get("biz_resp_msg");
        if ("S".equals(resp_code)) {
            if (!returnData.containsKey("dgtl_envlp")) {
                throw new Exception("缺少dgtl_envlp参数！");
            }
            String dgtl_envlp = RsaCodingUtil.decryptByPriPfxFile(returnData.get("dgtl_envlp"), priKeyPath, priKeyPassword);
            dgtl_envlp = SecurityUtil.Base64Decode(dgtl_envlp);
            String aes_key = StringUtil.getAesKey(dgtl_envlp);//获取返回的AES_KEY
            return SecurityUtil.Base64Decode(SecurityUtil.AesDecrypt(returnData.get("protocols"), aes_key));
        } else {
            throw new Exception("绑卡查询失败！[" + biz_resp_code + "]" + biz_resp_msg);
        }
    }

    @Override
    protected Map<String, String> abolishBindParam(PaymentChannel channel, String protocolNo, String userId) throws Exception {
        String pubKeyPath = channel.getPubKeyPath();
        String priKeyPath = channel.getPriKeyPath();
        String priKeyPassword = channel.getPriKeyPassword();
        String version = channel.getVersion();
        String memberNo = channel.getMemberNo();
        String terminalNo = channel.getTerminalNo();

        Map<String, String> dataArray = new TreeMap<>();
        dataArray.put("dgtl_envlp", getDgtlEnvlp(pubKeyPath));
        dataArray.put("send_time", DateUtil.getNowTime());
        dataArray.put("txn_type", "04");//交易类型
        dataArray.put("msg_id", StringUtil.getUUID());
        dataArray.put("version", version);
        dataArray.put("terminal_id", terminalNo);
        dataArray.put("member_id", memberNo);
        dataArray.put("user_id", userId);//用户唯一ID (和绑卡时一致)
        dataArray.put("protocol_no", encryptAES(protocolNo));//签约协议号（密文）

        String sign = sign(dataArray, priKeyPath, priKeyPassword);
        dataArray.put("signature", sign);//签名域
        return dataArray;
    }

    @Override
    protected boolean abolishBindProcess(PaymentChannel channel, Map<String, String> returnData) throws Exception {
        String resp_code = returnData.get("resp_code");
        String biz_resp_code = returnData.get("biz_resp_code");
        String biz_resp_msg = returnData.get("biz_resp_msg");
        if ("S".equals(resp_code)) {
            log.info("解绑成功！");
            return true;
        } else if ("F".equals(resp_code)) {
            throw new Exception("解绑失败！[" + biz_resp_code + "]" + biz_resp_msg);
        } else {
            throw new Exception("解绑未知！");
        }
    }

    /**
     * 获取公钥加密信封
     *
     * @pubKeyPath
     */
    private String getDgtlEnvlp(String pubKeyPath) throws Exception {
        //公钥加密
        return RsaCodingUtil.encryptByPubCerFile(SecurityUtil.Base64Encode(dgtl_envlp), pubKeyPath);
    }

    /**
     * aes 加密数据
     *
     * @data
     */
    private String encryptAES(String data) throws Exception {
        //先BASE64后进行AES加密
        return SecurityUtil.AesEncrypt(SecurityUtil.Base64Encode(data), aes_key);
    }

    /**
     * 私钥签名
     *
     * @param data
     * @param priKeyPath
     * @param priKeyPassword
     */
    private String sign(Map<String, String> data, String priKeyPath, String priKeyPassword) throws Exception {
        String signStr = UrlUtil.coverMap2String(data);
        String signature = SecurityUtil.sha1X16(signStr, DEFAULT_CHARSET);//签名
        return SignatureUtils.encryptByRSA(signature, priKeyPath, priKeyPassword);
    }
}
