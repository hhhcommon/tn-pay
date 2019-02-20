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
 * http请求流水表
 * </p>
 */
@TableName("pay_http_log")
public class HttpLog extends Model<HttpLog> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "log_id", type = IdType.AUTO)
    private Integer logId;
    /**
     * 支付指令id payment_id
     */
    @TableField("payment_id")
    private Integer paymentId;
    /**
     * 支付通道ID
     */
    @TableField("channel_id")
    private Integer channelId;
    /**
     * 支付步骤：1 支付，2 查询
     */
    @TableField("pay_action")
    private Integer payAction;
    /**
     * 请求报文
     */
    @TableField("request_message")
    private String requestMessage;
    /**
     * 请求状态。1请求中；2请求返回
     */
    @TableField("request_status")
    private Integer requestStatus;
    /**
     * 应答报文
     */
    @TableField("response_message")
    private String responseMessage;
    @TableField("create_time")
    private String createTime;
    @TableField("update_time")
    private String updateTime;


    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getPayAction() {
        return payAction;
    }

    public void setPayAction(Integer payAction) {
        this.payAction = payAction;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public Integer getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(Integer requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
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
        return this.logId;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
