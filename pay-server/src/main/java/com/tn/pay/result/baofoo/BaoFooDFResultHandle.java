package com.tn.pay.result.baofoo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.tn.pay.constant.BaoFooDFConstant;
import com.tn.pay.module.PaymentChannel;
import com.tn.pay.result.ResultHandle;
import com.tn.pay.rsa.RsaCodingUtil;
import com.tn.pay.utils.SecurityUtil;

import java.util.Map;

import static com.tn.pay.constant.BaoFooDFConstant.*;

/**
 * baofoo [代付]组装结果
 */
public class BaoFooDFResultHandle implements ResultHandle {

    @Override
    public Map<String, String> generateResult(PaymentChannel channel, String result) throws Exception {
        String pubKeyPath = channel.getPubKeyPath();
        // 报文错误明文返回
        if (!result.contains(TRANS_CONTENT)) {
            //密文返回
            //第一步：公钥解密
            //第二步：BASE64解密
            result = RsaCodingUtil.decryptByPubCerFile(result, pubKeyPath);
            result = SecurityUtil.Base64Decode(result);
        }
        JSONObject data_content = JSON.parseObject(result);
        JSONObject trans_content = data_content.getJSONObject(TRANS_CONTENT);
        JSONObject trans_head = trans_content.getJSONObject(TRANS_HEAD);
        String return_code = trans_head.getString(RETURN_CODE);
        String return_msg = trans_head.getString(RETURN_MSG);

        Map<String, String> map = Maps.newHashMap();
        map.put(RETURN_CODE, return_code);
        map.put(RETURN_MSG, return_msg);
        //除了报文错误都会返回消息体
        if (!BaoFooDFConstant.ERRORS.contains(return_code)) {
            JSONArray trans_reqDatas = trans_content.getJSONArray(TRANS_REQDATAS);
            JSONObject trans_reqData = trans_reqDatas.getJSONObject(0).getJSONObject(TRANS_REQDATA);

            map.put(TRANS_ORDERID, trans_reqData.getString(TRANS_ORDERID));
            map.put(TRANS_BATCHID, trans_reqData.getString(TRANS_BATCHID));
            map.put(TRANS_NO, trans_reqData.getString(TRANS_NO));
            map.put(TRANS_MONEY, trans_reqData.getString(TRANS_MONEY));
            map.put(TO_ACC_NAME, trans_reqData.getString(TO_ACC_NAME));
            map.put(TO_ACC_NO, trans_reqData.getString(TO_ACC_NO));
            map.put(TO_ACC_DEPT, trans_reqData.getString(TO_ACC_DEPT));
            //查询返回特性字段
            map.put(TRANS_FEE, trans_reqData.getString(TRANS_FEE));
            map.put(STATE, trans_reqData.getString(STATE));
            map.put(TRANS_STARTTIME, trans_reqData.getString(TRANS_STARTTIME));
            map.put(TRANS_ENDTIME, trans_reqData.getString(TRANS_ENDTIME));
            map.put(TRANS_REMARK, trans_reqData.getString(TRANS_REMARK));
        }
        return map;
    }
}
