package com.tn.pay.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * cib [代付，代扣]
 */
public class CibConstant {

    //---------------------------------------------------------------
    // 代付返回码
    //---------------------------------------------------------------
    public static final String SUCCESS = "E0000";//处理成功

    public static final String EXCEPTION = "E0001";//系统异常
    public static final String OVER_TIME = "E0002";//系统超时
    public static final String DATA_EXCEPTION = "E0004";//数据操作异常
    public static final String FTP_EXCEPTION = "E0004";//FTP 访问异常
    public static final String PROCESSING = "E0508";//订单处理中

    //待查询状态
    public static final List<String> WAIT_QUERY = new ArrayList<>();

    static {
        WAIT_QUERY.add(EXCEPTION);
        WAIT_QUERY.add(OVER_TIME);
        WAIT_QUERY.add(DATA_EXCEPTION);
        WAIT_QUERY.add(FTP_EXCEPTION);
        WAIT_QUERY.add(PROCESSING);
    }
}
