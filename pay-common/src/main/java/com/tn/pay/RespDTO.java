package com.tn.pay;

import lombok.Data;

import java.io.Serializable;

/**
 * 公用返回对象
 * Created by taokai on 2017/12/4.
 */
@Data
public class RespDTO<D, Q> implements Serializable {

    //状态
    private StatusEnum status;
    //返回数据
    private D data;
    //查询参数
    private Q query;
    //备注
    private String remark;


    /**
     * 成功
     */
    public static <D, Q> RespDTO<D, Q> success(D data, Q query) {
        return new RespDTO<>(data, query, StatusEnum.SUCCESS);
    }


    public static <D, Q> RespDTO<D, Q> success(D data, Q query, String remark) {
        return new RespDTO<>(data, query, StatusEnum.SUCCESS, remark);
    }

    /**
     * 失败
     */
    public static <D, Q> RespDTO<D, Q> fail(D data, Q query) {
        return new RespDTO<>(data, query, StatusEnum.FAIL);
    }

    public static <D, Q> RespDTO<D, Q> fail(D data, Q query, String remark) {
        return new RespDTO<>(data, query, StatusEnum.FAIL, remark);
    }


    public RespDTO() {
    }

    public RespDTO(D data, Q query, StatusEnum status) {
        this.status = status;
        this.data = data;
        this.query = query;
    }

    public RespDTO(D data, Q query, StatusEnum status, String remark) {
        this.status = status;
        this.data = data;
        this.query = query;
        this.remark = remark;
    }

    /**
     * 状态枚举
     */
    public enum StatusEnum {
        SUCCESS("200", "成功"),
        FAIL("500", "失败"),
        FILE_UNCHECK("300", "存在未审阅文件"),
        CONTRACT_UNSETTLE("301", "合同未结清");

        StatusEnum(String status, String msg) {
            this.status = status;
            this.msg = msg;
        }

        private String status;
        private String msg;

        public String getStatus() {
            return status;
        }

        public String getMsg() {
            return msg;
        }
    }

}
