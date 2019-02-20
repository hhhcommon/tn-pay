package com.tn.pay.module;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 待支付记录表
 * </p>
 */
@TableName("pay_wait_payment_record")
public class WaitPaymentRecord extends Model<WaitPaymentRecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "payment_id", type = IdType.AUTO)
    private Integer paymentId;
    /**
     * 唯一支付指令流水号
     */
    @TableField("call_flow")
    private String callFlow;
    /**
     * 外部进单号
     */
    @TableField("out_order_no")
    private String outOrderNo;
    /**
     * 内部申请单号
     */
    @TableField("apply_id")
    private Integer applyId;
    /**
     * 商户订单号_宝付
     */
    @TableField("trans_no")
    private String transNo;
    /**
     * 场景ID
     */
    @TableField("scene_id")
    private Integer sceneId;
    /**
     * 产品ID
     */
    @TableField("product_id")
    private Integer productId;
    /**
     * 信托项目ID
     */
    @TableField("funds_id")
    private Integer fundsId;
    /**
     * 资金来源ID
     */
    @TableField("provider_id")
    private Integer providerId;
    /**
     * 支付方式。0:信息查询，1:代付，2:代收，3:绑卡 协议支付
     */
    @TableField("payment_mode")
    private Integer paymentMode;
    /**
     * 支付通道ID
     */
    @TableField("channel_id")
    private Integer channelId;
    /**
     * 客户开户名称
     */
    @TableField("account_name")
    private String accountName;
    /**
     * 客户证件类型: 01身份证
     */
    @TableField("cert_type")
    private String certType;
    /**
     * 客户证件号码
     */
    @TableField("cert_no")
    private String certNo;
    /**
     * 客户手机号
     */
    private String mobile;
    /**
     * 客户银行卡号
     */
    @TableField("cust_account")
    private String custAccount;
    /**
     * 开户行编码
     */
    @TableField("bank_code")
    private String bankCode;
    /**
     * 银行名称
     */
    @TableField("bank_name")
    private String bankName;
    /**
     * 签约协议号
     */
    @TableField("protocol_no")
    private String protocolNo;
    /**
     * 客户唯一标识
     */
    @TableField("user_id")
    private String userId;
    /**
     * 1：对私 2：对公  兴业银行：0：对公 1：对私
     */
    @TableField("pub_orpri_flag")
    private Integer pubOrpriFlag;
    /**
     * 支付金额
     */
    @TableField("payment_amount")
    private BigDecimal paymentAmount;
    /**
     * 支付状态。0 等待处理；1支付中；2支付成功；3支付失败
     */
    @TableField("payment_status")
    private Integer paymentStatus;
    /**
     * 支付返回信息
     */
    @TableField("payment_msg")
    private String paymentMsg;
    /**
     * 下单时间
     */
    @TableField("order_date")
    private String orderDate;
    /**
     * 支付成功时间
     */
    @TableField("payment_time")
    private String paymentTime;
    /**
     * 作废标志 0作废 1正常
     */
    private Integer status;
    @TableField("create_time")
    private String createTime;
    @TableField("update_time")
    private String updateTime;


    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public String getCallFlow() {
        return callFlow;
    }

    public void setCallFlow(String callFlow) {
        this.callFlow = callFlow;
    }

    public String getOutOrderNo() {
        return outOrderNo;
    }

    public void setOutOrderNo(String outOrderNo) {
        this.outOrderNo = outOrderNo;
    }

    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public Integer getSceneId() {
        return sceneId;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getFundsId() {
        return fundsId;
    }

    public void setFundsId(Integer fundsId) {
        this.fundsId = fundsId;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public Integer getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(Integer paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCustAccount() {
        return custAccount;
    }

    public void setCustAccount(String custAccount) {
        this.custAccount = custAccount;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getProtocolNo() {
        return protocolNo;
    }

    public void setProtocolNo(String protocolNo) {
        this.protocolNo = protocolNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getPubOrpriFlag() {
        return pubOrpriFlag;
    }

    public void setPubOrpriFlag(Integer pubOrpriFlag) {
        this.pubOrpriFlag = pubOrpriFlag;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Integer getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMsg() {
        return paymentMsg;
    }

    public void setPaymentMsg(String paymentMsg) {
        this.paymentMsg = paymentMsg;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.paymentId;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
