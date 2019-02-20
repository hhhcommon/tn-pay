package com.tn.pay.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 账务系统推送放款(回款)明细
 */
@Data
public class PaymentOrderDTO implements Serializable {
    // ==================================================================
    // 数据流标志
    // ==================================================================
    /**
     * 必填 唯一支付指令流水(上游流水号、订单号)
     */
    @NotEmpty(message = "callFlow 不能为空")
    private String callFlow;
    /**
     * 必填 外部系统订单号
     */
    @NotEmpty(message = "outOrderNo 不能为空")
    private String outOrderNo;
    /**
     * 必填 申请单号、贷款编号
     */
    @NotNull(message = "applyId 不能为空")
    private Integer applyId;

    // ==================================================================
    // 支付管道
    // ==================================================================
    /**
     * 必填 支付方式 1:代付(放款)，2:代收(回款),3:绑卡,协议支付(回款)
     */
    @NotNull(message = "paymentMode 不能为空")
    private Integer paymentMode;
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
    // 用户基础信息
    // ==================================================================
    /**
     * 必填 证件类型: 01身份证
     */
    @NotNull(message = "certType 不能为空")
    private Integer certType;
    /**
     * 必填 证件号码
     */
    @NotEmpty(message = "certNo 不能为空")
    private String certNo;
    /**
     * 必填 客户账户名称
     */
    @NotEmpty(message = "accountName 不能为空")
    private String accountName;
    /**
     * 必填 手机号
     */
    @NotEmpty(message = "mobile 不能为空")
    private String mobile;

    // ==================================================================
    // 用户银行信息
    // ==================================================================
    /**
     * 必填 银行名称
     */
    @NotEmpty(message = "bankName 不能为空")
    private String bankName;
    /**
     * 必填 银行编码
     */
    @NotEmpty(message = "bankCode 不能为空")
    private String bankCode;
    /**
     * 必填 客户账户，银行卡或第三方账号
     */
    @NotEmpty(message = "custAccount 不能为空")
    private String custAccount;

    // ==================================================================
    // 交易信息
    // ==================================================================
    /**
     * 必填 付款(扣款)金额
     */
    @DecimalMin(value = "0.01", message = "paymentAmount 值不正确")
    private BigDecimal paymentAmount;
    /**
     * 必填 1：对私 2：对公  兴业银行：0：对公 1：对私
     */
    private String pubOrpriFlag;
}
