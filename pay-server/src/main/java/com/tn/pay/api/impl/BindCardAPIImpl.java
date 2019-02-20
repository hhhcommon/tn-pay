package com.tn.pay.api.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tn.pay.agent.Caller;
import com.tn.pay.api.BindCardAPI;
import com.tn.pay.dto.BindCardDTO;
import com.tn.pay.manager.baofoo.BindCardManager;
import com.tn.pay.utils.StringUtil;
import com.tn.pay.utils.UniqueUtil;
import com.tn.pay.RespDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 绑卡接口
 */
@Slf4j
@Component
@Service(interfaceClass = BindCardAPI.class)
public class BindCardAPIImpl implements BindCardAPI {

    @Autowired
    private BindCardManager bindCardManager;

    @Override
    @Caller(name = "预绑卡")
    public Boolean readySign(BindCardDTO dto) {
        try {
            //唯一锁，保证同一个账号同一时间不重复发送
            UniqueUtil.exist(dto.getBankAccount());
            //生成对称密钥
            bindCardManager.setAesKey(StringUtil.getStringRandom(16));
            //执行
            bindCardManager.readySign(dto);
        } catch (Exception e) {
            return Boolean.FALSE;
        } finally {
            UniqueUtil.clean(dto.getBankAccount());
        }
        return Boolean.TRUE;
    }

    @Override
    @Caller(name = "确认绑卡")
    public Boolean confirmSign(BindCardDTO dto) {
        try {
            UniqueUtil.exist(dto.getBankAccount());
            bindCardManager.setAesKey(StringUtil.getStringRandom(16));
            bindCardManager.confirmSign(dto);
        } catch (Exception e) {
            return Boolean.FALSE;
        } finally {
            UniqueUtil.clean(dto.getBankAccount());
        }
        return Boolean.TRUE;
    }

    @Override
    @Caller(name = "绑卡查询")
    public Boolean queryBind(BindCardDTO dto) {
        try {
            UniqueUtil.exist(dto.getBankAccount());
            bindCardManager.setAesKey(StringUtil.getStringRandom(16));
            bindCardManager.queryBind(dto);
        } catch (Exception e) {
            return Boolean.FALSE;
        } finally {
            UniqueUtil.clean(dto.getBankAccount());
        }
        return Boolean.TRUE;
    }

    @Override
    @Caller(name = "解除绑卡")
    public Boolean abolishBind(BindCardDTO dto) {
        try {
            UniqueUtil.exist(dto.getBankAccount());
            bindCardManager.setAesKey(StringUtil.getStringRandom(16));
            bindCardManager.abolishBind(dto);
        } catch (Exception e) {
            return Boolean.FALSE;
        } finally {
            UniqueUtil.clean(dto.getBankAccount());
        }
        return Boolean.TRUE;
    }

    //TODO 上传绑卡信息
}
