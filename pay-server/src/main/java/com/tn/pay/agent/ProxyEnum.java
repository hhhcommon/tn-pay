package com.tn.pay.agent;

import com.tn.pay.agent.impl.SimpleProxy;
import com.tn.pay.agent.impl.TimeProxy;
import com.tn.pay.agent.impl.ValidProxy;

/**
 * 代理处理类枚举
 */
public enum ProxyEnum {
    DEFAULT(new SimpleProxy()),
    TIME(new TimeProxy()),
    VALID(new ValidProxy());

    private Proxy proxy;

    ProxyEnum(Proxy proxy) {
        this.proxy = proxy;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }
}
