package com.ruyuan.eshop.inventory.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 同步商品sku库存数据到缓存
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncStockToCacheRequest implements Serializable {

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
}
