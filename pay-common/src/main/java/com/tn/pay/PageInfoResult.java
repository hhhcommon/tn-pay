package com.tn.pay;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lijianzheng on 2017/12/7.
 */
@Data
public class PageInfoResult<T> implements Serializable {


    private Long total;

    private List<T> rows;

}
