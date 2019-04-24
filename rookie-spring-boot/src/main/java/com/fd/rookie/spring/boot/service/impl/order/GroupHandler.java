package com.fd.rookie.spring.boot.service.impl.order;

import com.fd.rookie.spring.boot.annotation.HandlerType;
import com.fd.rookie.spring.boot.po.order.TOrder;
import com.fd.rookie.spring.boot.service.order.AbstractHandlerOrder;
import org.springframework.stereotype.Component;

@Component
@HandlerType("2")
public class GroupHandler implements AbstractHandlerOrder {
    @Override
    public String orderHandler(TOrder tOrder) {
        return "处理团购订单";
    }
}
