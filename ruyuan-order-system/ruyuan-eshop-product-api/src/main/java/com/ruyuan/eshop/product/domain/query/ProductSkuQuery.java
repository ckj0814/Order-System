package com.ruyuan.eshop.product.domain.query;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class ProductSkuQuery implements Serializable {

    private static final long serialVersionUID = 4788741095015777932L;

    /**
     * 卖家ID
     */
    private String sellerId;

    /**
     * 商品skuCode集合
     */
    private List<String> skuCodeList;
}