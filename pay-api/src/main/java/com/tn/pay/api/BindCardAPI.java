package com.tn.pay.api;

import com.tn.pay.dto.BindCardDTO;

public interface BindCardAPI {
    /**
     * 预绑卡
     */
    Boolean readySign(BindCardDTO dto);

    /**
     * 确认绑卡
     */
    Boolean confirmSign(BindCardDTO dto);

    /**
     * 绑卡查询
     */
    Boolean queryBind(BindCardDTO dto);

    /**
     * 解绑卡
     */
    Boolean abolishBind(BindCardDTO dto);
}
