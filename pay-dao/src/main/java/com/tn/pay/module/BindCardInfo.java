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
@TableName("pay_bind_card_info")
public class BindCardInfo extends Model<BindCardInfo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "bind_id", type = IdType.AUTO)
    private Integer bindId;
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
     * 支付通道ID
     */
    @TableField("channel_id")
    private Integer channelId;
    /**
     * 用户在系统中唯一id
     */
    @TableField("user_id")
    private String userId;
    /**
     * 银行卡号
     */
    @TableField("bank_account")
    private String bankAccount;
    /**
     * 客户姓名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 证件号码
     */
    @TableField("cert_no")
    private String certNo;
    /**
     * 客户手机
     */
    private String mobile;
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
     * 证件类型
     */
    @TableField("cert_type")
    private String certType;
    /**
     * 卡类型 101 借记卡|102 信用卡
     */
    @TableField("bank_card_type")
    private String bankCardType;
    /**
     * 银行卡安全码 000[信用卡]
     */
    @TableField("bank_security_code")
    private String bankSecurityCode;
    /**
     * 银行卡有效期 yyMM[信用卡]
     */
    @TableField("bank_valid_thru")
    private String bankValidThru;
    /**
     * 证件有效期
     */
    @TableField("cert_valid_thru")
    private String certValidThru;
    /**
     * 预签约唯一码 
     */
    @TableField("unique_code")
    private String uniqueCode;
    /**
     * 签约协议号
     */
    @TableField("protocol_code")
    private String protocolCode;
    @TableField("create_time")
    private String createTime;
    @TableField("update_time")
    private String updateTime;
    /**
     * 状态：1 预绑卡中，2 绑卡成功，3 确认绑卡中 0 作废
     */
    private Integer status;


    public Integer getBindId() {
        return bindId;
    }

    public void setBindId(Integer bindId) {
        this.bindId = bindId;
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

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getBankCardType() {
        return bankCardType;
    }

    public void setBankCardType(String bankCardType) {
        this.bankCardType = bankCardType;
    }

    public String getBankSecurityCode() {
        return bankSecurityCode;
    }

    public void setBankSecurityCode(String bankSecurityCode) {
        this.bankSecurityCode = bankSecurityCode;
    }

    public String getBankValidThru() {
        return bankValidThru;
    }

    public void setBankValidThru(String bankValidThru) {
        this.bankValidThru = bankValidThru;
    }

    public String getCertValidThru() {
        return certValidThru;
    }

    public void setCertValidThru(String certValidThru) {
        this.certValidThru = certValidThru;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getProtocolCode() {
        return protocolCode;
    }

    public void setProtocolCode(String protocolCode) {
        this.protocolCode = protocolCode;
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
        return this.bindId;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
