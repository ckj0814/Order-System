package com.ruyuan.eshop.order.wms;


import com.ruyuan.eshop.common.enums.OrderStatusChangeEnum;
import com.ruyuan.eshop.order.dao.OrderInfoDAO;
import com.ruyuan.eshop.order.dao.OrderOperateLogDAO;
import com.ruyuan.eshop.order.domain.dto.WmsShipDTO;
import com.ruyuan.eshop.order.domain.entity.OrderInfoDO;
import com.ruyuan.eshop.order.exception.OrderBizException;
import com.ruyuan.eshop.order.service.impl.OrderOperateLogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractWmsShipResultProcessor implements OrderWmsShipResultProcessor {

    @Autowired
    protected OrderInfoDAO orderInfoDAO;

    @Autowired
    protected OrderOperateLogFactory orderOperateLogFactory;

    @Autowired
    private OrderOperateLogDAO orderOperateLogDAO;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void execute(WmsShipDTO wmsShipDTO) throws OrderBizException {

        //1、查询订单
        OrderInfoDO order = orderInfoDAO.getByOrderId(wmsShipDTO.getOrderId());
        if (null == order) {
            return;
        }

        //2、校验订单状态
        if (!checkOrderStatus(order)) {
            return;
        }

        //3、执行具体的业务逻辑
        doExecute(wmsShipDTO, order);

        //4、更新订单状态
        changeOrderStatus(order, wmsShipDTO);

        //5、增加操作日志
        saveOrderOperateLog(order, wmsShipDTO);
    }

    /**
     * 校验订单状态
     *
     * @param order
     * @throws OrderBizException
     */
    protected abstract boolean checkOrderStatus(OrderInfoDO order) throws OrderBizException;

    /**
     * 执行具体的业务逻辑
     *
     * @param wmsShipDTO
     * @param order
     */
    protected abstract void doExecute(WmsShipDTO wmsShipDTO, OrderInfoDO order);

    /**
     * 更新订单状态
     *
     * @param order
     */
    private void changeOrderStatus(OrderInfoDO order, WmsShipDTO wmsShipDTO) {
        //todo 状态机
        OrderStatusChangeEnum statusChange = wmsShipDTO.getStatusChange();
        orderInfoDAO.updateOrderStatus(order.getOrderId(), statusChange.getPreStatus().getCode()
                , statusChange.getCurrentStatus().getCode());
    }

    /**
     * 增加订单操作日志
     *
     * @param order
     */
    private void saveOrderOperateLog(OrderInfoDO order, WmsShipDTO wmsShipDTO) {
        orderOperateLogDAO.save(orderOperateLogFactory.get(order, wmsShipDTO.getStatusChange()));
    }

}
