package com.fd.rookie.spring.boot.service.order;

import com.fd.rookie.spring.boot.po.order.TOrder;

/**
 * 订单处理抽象类
 */
public interface AbstractHandlerOrder {
    /**
     * 订单处理
     * @param tOrder
     * @return
     */
    String orderHandler(TOrder tOrder);
}
