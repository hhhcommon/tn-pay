package com.tn.pay.manager;

import com.alibaba.fastjson.JSON;
import com.tn.pay.agent.Caller;
import com.tn.pay.enums.PaymentCompanyEnum;
import com.tn.pay.enums.PaymentModeEnum;
import com.tn.pay.enums.PaymentStatusEnum;
import com.tn.pay.module.HttpLog;
import com.tn.pay.module.PaymentChannel;
import com.tn.pay.module.WaitPaymentRecord;
import com.tn.pay.param.ParamHandle;
import com.tn.pay.param.baofoo.*;
import com.tn.pay.param.cib.CibParamHandle;
import com.tn.pay.param.cib.CibQueryHandle;
import com.tn.pay.process.ResultProcessor;
import com.tn.pay.process.baofoo.BaoFooBindPayResultProcessor;
import com.tn.pay.process.baofoo.BaoFooDFResultProcessor;
import com.tn.pay.process.baofoo.BaoFooDSResultProcessor;
import com.tn.pay.process.cib.CibResultProcessor;
import com.tn.pay.result.ResultHandle;
import com.tn.pay.result.baofoo.BaoFooBindPayResultHandle;
import com.tn.pay.result.baofoo.BaoFooDFResultHandle;
import com.tn.pay.result.baofoo.BaoFooDSResultHandle;
import com.tn.pay.result.cib.CibResultHandle;
import com.tn.pay.service.HttpLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * 支付父类
 * 1 获取支付管道
 * 2 组装处理器，子类可重写
 * 3 组装参数
 * 4 保存log
 * 5 请求接口，子类实现
 * 6 保存log
 * 7 处理结果
 */
@Slf4j
public abstract class PaymentManager {

    @Autowired
    private HttpLogService httpLogService;
    private WaitPaymentRecord record;

    protected ParamHandle paramHandle;
    protected ResultHandle resultHandle;
    protected ResultProcessor resultProcessor;

    public void setRecord(WaitPaymentRecord record) {
        this.record = record;
    }

    @Transactional
    @Caller(name = "支付")
    public void payment() throws Exception {
        log.info("支付-流水号:{}，支付指令:{}", this.record.getCallFlow(), this.record);
        Assert.notNull(this.record, "没有设置支付指令！");
        Integer channelId = this.record.getChannelId();
        Integer paymentId = this.record.getPaymentId();
        //获取支付管道
        PaymentChannel channel = new PaymentChannel();
        channel.setChannelId(channelId);
        channel = channel.selectById();
        log.info("支付-流水号:{}，处理器组装process:{}", record.getCallFlow(), channel.getPayCompanyName());
        log.info("支付-流水号:{}，处理器组装mode:{}", record.getCallFlow(), PaymentModeEnum.getName(channel.getPaymentMode()));
        //组装处理器
        initBean(this.record, channel);
        //组装参数
        Map<String, Object> param = paramHandle.generateParam(this.record, channel);
        log.info("支付-流水号:{}，组装参数param:{}", record.getCallFlow(), JSON.toJSONString(param));
        //保存log
        Integer payAction = null;
        if (PaymentStatusEnum.NOT_PAY_0.getStatus().equals(record.getPaymentStatus())) {
            payAction = 1;//支付动作
        } else if (PaymentStatusEnum.WAIT_PAY_1.getStatus().equals(record.getPaymentStatus())) {
            payAction = 2;//查询动作
        }
        HttpLog httpLog = httpLogService.createLog(param, channelId, paymentId, payAction);
        //请求接口
        String result = payment(this.record, channel, param);
        //处理结果集
        Map<String, String> map = resultHandle.generateResult(channel, result);
        log.info("支付-流水号:{}，组装结果map:{}", record.getCallFlow(), JSON.toJSONString(map));
        //保存log
        httpLogService.updateLog(httpLog, map);
        //处理结果
        resultProcessor.process(this.record, channel, map);
    }

    /**
     * 组装代付，代扣，的支付，查询处理类
     */
    protected void initBean(WaitPaymentRecord record, PaymentChannel channel) {
        if (PaymentCompanyEnum.BAO_FOO.getCode().equals(channel.getPayCompanyId())) {
            if (PaymentModeEnum.PAYMENT.getCode().equals(channel.getPaymentMode())) {
                if (PaymentStatusEnum.WAIT_PAY_1.getStatus().equals(record.getPaymentStatus())) {
                    this.paramHandle = new BaoFooDFQueryHandle();
                } else if (PaymentStatusEnum.NOT_PAY_0.getStatus().equals(record.getPaymentStatus())) {
                    this.paramHandle = new BaoFooDFParamHandle();
                }
                this.resultHandle = new BaoFooDFResultHandle();
                this.resultProcessor = new BaoFooDFResultProcessor();
            } else if (PaymentModeEnum.REPAYMENT.getCode().equals(channel.getPaymentMode())) {
                if (PaymentStatusEnum.WAIT_PAY_1.getStatus().equals(record.getPaymentStatus())) {
                    this.paramHandle = new BaoFooDSQueryHandle();
                } else if (PaymentStatusEnum.NOT_PAY_0.getStatus().equals(record.getPaymentStatus())) {
                    this.paramHandle = new BaoFooDSParamHandle();
                }
                this.resultHandle = new BaoFooDSResultHandle();
                this.resultProcessor = new BaoFooDSResultProcessor();
            } else if (PaymentModeEnum.BIND_PAY.getCode().equals(channel.getPaymentMode())) {
                if (PaymentStatusEnum.WAIT_PAY_1.getStatus().equals(record.getPaymentStatus())) {
                    this.paramHandle = new BaoFooBindPayQueryHandle();
                } else if (PaymentStatusEnum.NOT_PAY_0.getStatus().equals(record.getPaymentStatus())) {
                    this.paramHandle = new BaoFooBindPayParamHandle();
                }
                this.resultHandle = new BaoFooBindPayResultHandle();
                this.resultProcessor = new BaoFooBindPayResultProcessor();
            }
        } else if (PaymentCompanyEnum.CIB.getCode().equals(channel.getPayCompanyId())) {
            if (PaymentModeEnum.PAYMENT.getCode().equals(channel.getPaymentMode())) {
                if (PaymentStatusEnum.WAIT_PAY_1.getStatus().equals(record.getPaymentStatus())) {
                    this.paramHandle = new CibQueryHandle();
                } else if (PaymentStatusEnum.NOT_PAY_0.getStatus().equals(record.getPaymentStatus())) {
                    this.paramHandle = new CibParamHandle();
                }
            } else if (PaymentModeEnum.REPAYMENT.getCode().equals(channel.getPaymentMode())) {
                if (PaymentStatusEnum.WAIT_PAY_1.getStatus().equals(record.getPaymentStatus())) {
                    this.paramHandle = new CibQueryHandle();
                } else if (PaymentStatusEnum.NOT_PAY_0.getStatus().equals(record.getPaymentStatus())) {
                    this.paramHandle = new CibParamHandle();
                }
            }
            this.resultProcessor = new CibResultProcessor();
            this.resultHandle = new CibResultHandle();
        }
    }

    protected abstract String payment(WaitPaymentRecord record, PaymentChannel channel, Map<String, Object> param) throws Exception;
}
