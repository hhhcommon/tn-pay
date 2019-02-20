package com.tn.pay;

import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 公用的分页插件
 * Created by taokai on 2017/12/9.
 */
@Data
public class EasyUiDataGridResult implements Serializable {

    private long total;//总条数

    private List<?> rows;//查询到的记录


    public EasyUiDataGridResult() {
    }

    public EasyUiDataGridResult(PageInfo<?> pageInfo) {
        this.total = pageInfo.getTotal();
        this.rows = pageInfo.getList();
    }

    public static EasyUiDataGridResult buildResultByList(List dataList) {
        PageInfo<?> pageInfo = new PageInfo<>(dataList);
        return new EasyUiDataGridResult(pageInfo);
    }

    /**
     * 原始数据经过转换后使用
     */
    public static EasyUiDataGridResult buildResultByList(List dataList, List originList) {
        PageInfo<?> pageInfo = new PageInfo<>(originList);
        EasyUiDataGridResult gridResult = new EasyUiDataGridResult(pageInfo);
        gridResult.setRows(dataList);
        return gridResult;
    }
}
