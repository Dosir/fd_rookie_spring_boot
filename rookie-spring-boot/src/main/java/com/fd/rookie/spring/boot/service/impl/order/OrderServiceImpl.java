package com.fd.rookie.spring.boot.service.impl.order;

import com.fd.rookie.spring.boot.po.order.TOrder;
import com.fd.rookie.spring.boot.service.order.OrderService;

/**
 * 常规的订单处理逻辑
 */
public class OrderServiceImpl implements OrderService {
    @Override
    public String handleOrder(TOrder order) {
        String type = order.getType();
        if ("1".equals(type)) {
            return "处理普通订单";
        } else if ("2".equals(type)) {
            return "处理团购订单";
        } else if ("3".equals(type)) {
            return "处理促销订单";
        }
        return null;
    }
}
