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
 * 支付通道表
 * </p>
 */
@TableName("pay_payment_channel")
public class PaymentChannel extends Model<PaymentChannel> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "channel_id", type = IdType.AUTO)
    private Integer channelId;
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
     * 第三方支付公司ID
     */
    @TableField("pay_company_id")
    private Integer payCompanyId;
    /**
     * 第三方支付公司名称
     */
    @TableField("pay_company_name")
    private String payCompanyName;
    /**
     * 商户号
     */
    @TableField("member_no")
    private String memberNo;
    /**
     * 商户注册key，余额功能
     */
    @TableField("member_key")
    private String memberKey;
    /**
     * 终端号
     */
    @TableField("terminal_no")
    private String terminalNo;
    /**
     * 公钥路径
     */
    @TableField("pub_key_path")
    private String pubKeyPath;
    /**
     * 私钥路径
     */
    @TableField("pri_key_path")
    private String priKeyPath;
    /**
     * 私钥密码
     */
    @TableField("pri_key_password")
    private String priKeyPassword;
    /**
     * 数据请求类型 json/xml
     */
    @TableField("data_type")
    private String dataType;
    /**
     * 接口版本
     */
    private String version;
    /**
     * 接口地址
     */
    @TableField("request_url")
    private String requestUrl;
    /**
     * 状态，0废弃，1线上，2测试
     */
    private Integer status;


    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
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

    public Integer getPayCompanyId() {
        return payCompanyId;
    }

    public void setPayCompanyId(Integer payCompanyId) {
        this.payCompanyId = payCompanyId;
    }

    public String getPayCompanyName() {
        return payCompanyName;
    }

    public void setPayCompanyName(String payCompanyName) {
        this.payCompanyName = payCompanyName;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getMemberKey() {
        return memberKey;
    }

    public void setMemberKey(String memberKey) {
        this.memberKey = memberKey;
    }

    public String getTerminalNo() {
        return terminalNo;
    }

    public void setTerminalNo(String terminalNo) {
        this.terminalNo = terminalNo;
    }

    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public String getPriKeyPath() {
        return priKeyPath;
    }

    public void setPriKeyPath(String priKeyPath) {
        this.priKeyPath = priKeyPath;
    }

    public String getPriKeyPassword() {
        return priKeyPassword;
    }

    public void setPriKeyPassword(String priKeyPassword) {
        this.priKeyPassword = priKeyPassword;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    protected Serializable pkVal() {
        return this.channelId;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
