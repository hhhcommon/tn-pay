package com.tn.pay.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 通道标识DTO
 */
@Data
public class ChannelModuleDTO implements Serializable {
    /**
     * 必填 场景ID
     */
    @NotNull(message = "sceneId 不能为空")
    private Integer sceneId;
    /**
     * 必填 信托项目ID
     */
    @NotNull(message = "fundsId 不能为空")
    private Integer fundsId;
    /**
     * 必填 产品ID
     */
    @NotNull(message = "productId 不能为空")
    private Integer productId;
    /**
     * 必填 资金来源ID
     */
    @NotNull(message = "providerId 不能为空")
    private Integer providerId;
    /**
     * 支付方式。0:信息查询，1:代付，2:代收，3:绑卡,协议支付
     */
    private Integer paymentMode;

    public ChannelModuleDTO(Integer sceneId, Integer fundsId, Integer productId, Integer providerId) {
        this.sceneId = sceneId;
        this.fundsId = fundsId;
        this.productId = productId;
        this.providerId = providerId;
    }
}
