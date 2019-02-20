package com.tn.pay.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 绑卡数据
 */
@Data
public class BindCardDTO implements Serializable {

    // ==================================================================
    // 支付管道
    // ==================================================================
    /**
     * 必填 场景ID
     */
    @NotNull(message = "sceneId 不能为空")
    private Integer sceneId;
    /**
     * 必填 资金(信托项目)ID
     */
    @NotNull(message = "fundsId 不能为空")
    private Integer fundsId;
    /**
     * 必填 资金来源(方)ID
     */
    @NotNull(message = "providerId 不能为空")
    private Integer providerId;
    /**
     * 必填 产品ID
     */
    @NotNull(message = "productId 不能为空")
    private Integer productId;

    // ==================================================================
    // 预绑信息
    // ==================================================================
    /**
     * 必填 银行卡号
     */
    private String bankAccount;
    /**
     * 必填 用户在系统中唯一id
     */
    private String userId;
    /**
     * 必填 客户姓名
     */
    private String userName;
    /**
     * 必填 证件号码
     */
    private String certNo;
    /**
     * 必填 客户手机
     */
    private String mobile;
    /**
     * 必填 证件类型
     */
    private String certType;
    /**
     * 必填 卡类型 101 借记卡|102 信用卡
     */
    private Integer bankCardType;
    /**
     * 可选 银行卡安全码 000[信用卡]
     */
    private String bankSecurityCode;
    /**
     * 可选 银行卡有效期 yyMM[信用卡]
     */
    private String bankValidThru;
    /**
     * 可选 证件有效期
     */
    private String certValidThru;

    // ==================================================================
    // 确认绑卡信息
    // ==================================================================
    /**
     * 短信码
     */
    private String sms;
}
