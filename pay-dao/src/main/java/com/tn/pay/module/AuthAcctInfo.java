package com.tn.pay.module;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 绑卡信息表
 * </p>
 */
@TableName("pay_auth_acct_info")
public class AuthAcctInfo extends Model<AuthAcctInfo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "auth_id", type = IdType.AUTO)
    private Integer authId;
    /**
     * 唯一支付指令流水号
     */
    @TableField("call_flow")
    private String callFlow;
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
     * 支付方式。0:信息查询，1:代付，2:代收
     */
    @TableField("payment_mode")
    private Integer paymentMode;
    /**
     * 支付通道ID
     */
    @TableField("channel_id")
    private Integer channelId;
    /**
     * 证件类型
     */
    @TableField("cert_type")
    private String certType;
    /**
     * 证件号码
     */
    @TableField("cert_no")
    private String certNo;
    /**
     * 客户姓名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 客户手机
     */
    private String mobile;
    /**
     * 银行卡号
     */
    @TableField("bank_account")
    private String bankAccount;
    /**
     * 银行名称
     */
    @TableField("bank_name")
    private String bankName;
    /**
     * 开户行编码
     */
    @TableField("bank_code")
    private String bankCode;
    @TableField("create_time")
    private String createTime;
    @TableField("update_time")
    private String updateTime;
    /**
     * 状态：1 成功，0 失败
     */
    private Integer status;


    public Integer getAuthId() {
        return authId;
    }

    public void setAuthId(Integer authId) {
        this.authId = authId;
    }

    public String getCallFlow() {
        return callFlow;
    }

    public void setCallFlow(String callFlow) {
        this.callFlow = callFlow;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    protected Serializable pkVal() {
        return this.authId;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
