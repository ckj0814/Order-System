package com.ruyuan.eshop.inventory.domain.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 添加商品库存
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class AddProductStockRequest implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 更新时间
     */
    private Date gmtModified;
    /**
     * 商品sku编号
     */
    private String skuCode;

    /**
     * 销售库存
     */
    private Long saleStockQuantity;

}
