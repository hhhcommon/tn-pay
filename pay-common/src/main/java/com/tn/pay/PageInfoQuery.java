package com.tn.pay;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by lijianzheng on 2017/12/7.
 */
@Data
public class PageInfoQuery<T> implements Serializable {

    private Integer rows;

    private Integer page;

    private T params;

}
