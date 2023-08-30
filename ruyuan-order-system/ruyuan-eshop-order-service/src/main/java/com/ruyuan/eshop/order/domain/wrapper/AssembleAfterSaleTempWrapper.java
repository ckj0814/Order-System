package com.ruyuan.eshop.order.domain.wrapper;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ruyuan.eshop.order.domain.entity.AfterSaleInfoDO;
import com.ruyuan.eshop.order.domain.entity.AfterSaleLogDO;
import com.ruyuan.eshop.order.domain.entity.OrderInfoDO;
import lombok.Data;

import java.io.Serializable;

/**
 * 组装临时售后更新订单数据包装器
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class AssembleAfterSaleTempWrapper implements Serializable {
    private static final long serialVersionUID = 1L;

    private OrderInfoDO orderInfoDO;

    private UpdateWrapper updateWrapper;

    private AfterSaleLogDO afterSaleLogDO;

    private AfterSaleInfoDO afterSaleInfoDO;
}
