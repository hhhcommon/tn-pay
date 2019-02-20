package com.tn.pay.param.baofoo;

import com.google.common.collect.ImmutableMap;
import com.tn.pay.module.PaymentChannel;
import com.tn.pay.module.WaitPaymentRecord;
import com.tn.pay.param.ParamHandle;

import java.util.*;

import static com.tn.pay.constant.BaoFooDFConstant.*;

/**
 * 支付[代付]参数组装父类
 */
public abstract class BaoFooDFHandle implements ParamHandle {

    @Override
    public Map<String, Object> generateParam(WaitPaymentRecord record, PaymentChannel channel) {
        return generateDFParam(Collections.singletonList(record));
    }

    private Map<String, Object> generateDFParam(List<WaitPaymentRecord> records) {
        Map<String, Object> data = new HashMap<>();
        Map<String, List> trans_content = new HashMap<>();

        List<Map> trans_reqDatas = new ArrayList<>();
        List<Map> trans_reqData = new ArrayList<>();
        records.forEach(record -> {
            Map<String, String> dataMap = getData(record);
            trans_reqData.add(dataMap);
        });
        trans_reqDatas.add(ImmutableMap.of(TRANS_REQDATA, trans_reqData));

        trans_content.put(TRANS_REQDATAS, trans_reqDatas);
        data.put(TRANS_CONTENT, trans_content);
        return data;
    }

    protected abstract Map<String, String> getData(WaitPaymentRecord waitPaymentRecord);
}
