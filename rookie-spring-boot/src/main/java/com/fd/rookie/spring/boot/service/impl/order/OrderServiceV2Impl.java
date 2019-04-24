package com.fd.rookie.spring.boot.service.impl.order;

import com.fd.rookie.spring.boot.po.order.TOrder;
import com.fd.rookie.spring.boot.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 使用策略模式的订单处理逻辑
 */
@Service
public class OrderServiceV2Impl implements OrderService{
    @Autowired
    private HandlerOrderContext handlerOrderContext;

    @Override
    public String handleOrder(TOrder order) {
        return handlerOrderContext.orderHandler(order);
    }
}
