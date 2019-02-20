package com.tn.pay.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tn.pay.ApplicationContextProvider;
import com.tn.pay.api.impl.BindCardAPIImpl;
import com.tn.pay.dto.BindCardDTO;
import com.tn.pay.manager.baofoo.BaoFooManager;
import com.tn.pay.module.BindCardInfo;
import com.tn.pay.module.WaitPaymentRecord;
import com.tn.pay.utils.UniqueUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 支付定时任务
 */
@Slf4j
@Component
public class PaymentTask {

    //设置最合适的线程竞争数
    private static final Integer threads = 3;//TODO 线程数决定

    private static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(threads);

    @Autowired
    private BindCardAPIImpl bindCardAPI;

    /**
     * 支付定时任务
     */
    //@Scheduled(cron = "0 0/3 * * * ?")
    public void payment() {
        log.info("支付-定时任务开始！");
        try {
            UniqueUtil.exist("PAYMENT");
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.in("payment_status", Arrays.asList(0, 1));
            wrapper.eq("status", 1);
            List<WaitPaymentRecord> waitPaymentRecords = new WaitPaymentRecord().selectList(wrapper);
            log.info("支付-定时任务数量:{}", waitPaymentRecords.size());
            CountDownLatch latch = new CountDownLatch(waitPaymentRecords.size());
            waitPaymentRecords.forEach(record -> {
                //多例执行，一个任务独占一份定制资源
                BaoFooManager bean = ApplicationContextProvider.getBean(BaoFooManager.class);
                bean.setRecord(record);
                fixedThreadPool.execute(() -> {
                    try {
                        bean.payment();
                    } catch (Exception e) {
                        log.error("支付-异常失败-流水号:{}，异常原因:", record.getCallFlow(), e);
                    } finally {
                        latch.countDown();
                    }
                });
            });
        } catch (Exception e) {
            log.error("支付-定时任务异常", e);
        } finally {
            UniqueUtil.clean("PAYMENT");
        }
    }

    /**
     * 绑卡定时任务
     */
    //@Scheduled(cron = "0 0/3 * * * ?")
    public void queryBind() {
        log.info("绑卡-定时任务开始！");
        try {
            UniqueUtil.exist("QUERY_BIND");
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.eq("status", 3);
            List<BindCardInfo> bindCardInfos = new BindCardInfo().selectList(wrapper);
            log.info("绑卡-定时任务数量:{}", bindCardInfos.size());
            bindCardInfos.forEach(bindCardInfo -> {
                BindCardDTO bindCardDTO = new BindCardDTO();
                BeanUtils.copyProperties(bindCardInfo, bindCardDTO);
                bindCardAPI.queryBind(bindCardDTO);
            });
        } catch (Exception e) {
            log.error("绑卡-定时任务异常", e);
        } finally {
            UniqueUtil.clean("QUERY_BIND");
        }
    }

}
