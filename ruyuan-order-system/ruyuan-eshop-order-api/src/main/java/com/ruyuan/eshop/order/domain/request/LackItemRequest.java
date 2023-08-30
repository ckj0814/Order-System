package com.ruyuan.eshop.order.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * 具体的缺品项
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LackItemRequest implements Serializable {

    /**
     * sku编码
     */
    private String skuCode;

    /**
     * 缺品数量
     */
    private Integer lackNum;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LackItemRequest that = (LackItemRequest) o;
        return Objects.equals(skuCode, that.skuCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(skuCode);
    }
}
