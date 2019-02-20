package com.tn.pay.result.baofoo;

import com.tn.pay.constant.BaoFooBindPayConstant;
import com.tn.pay.module.PaymentChannel;
import com.tn.pay.result.ResultHandle;
import com.tn.pay.rsa.SignatureUtils;
import com.tn.pay.utils.SecurityUtil;
import com.tn.pay.utils.UrlUtil;

import java.util.Map;

/**
 * baofoo [协议支付]组装结果
 */
public class BaoFooBindPayResultHandle implements ResultHandle {

    @Override
    public Map<String, String> generateResult(PaymentChannel channel, String result) throws Exception {
        String pubKeyPath = channel.getPubKeyPath();
        Map<String, String> params = UrlUtil.getParams(result);

        if (!params.containsKey(BaoFooBindPayConstant.SIGNATURE)) {
            throw new Exception("缺少验签参数！");
        }
        String rSign = params.get(BaoFooBindPayConstant.SIGNATURE);
        params.remove(BaoFooBindPayConstant.SIGNATURE);//需要删除签名字段
        String rSignVStr = UrlUtil.coverMap2String(params);
        String rSignature = SecurityUtil.sha1X16(rSignVStr, "UTF-8");//签名

        if (!SignatureUtils.verifySignature(pubKeyPath, rSignature, rSign)) {
            throw new Exception("验签失败！");
        }
        if (!params.containsKey(BaoFooBindPayConstant.RESP_CODE)) {
            throw new Exception("缺少resp_code参数！");
        }
        return params;
    }
}
