package com.fd.rookie.spring.boot.service.order;

import com.fd.rookie.spring.boot.po.order.TOrder;

public interface OrderService {
    /**
     * 根据订单的不同类型进行不同的处理
     * @param order
     * @return
     */
    String handleOrder(TOrder order);
}
