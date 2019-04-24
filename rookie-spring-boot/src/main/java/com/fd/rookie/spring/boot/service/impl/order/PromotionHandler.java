package com.fd.rookie.spring.boot.service.impl.order;

import com.fd.rookie.spring.boot.annotation.HandlerType;
import com.fd.rookie.spring.boot.po.order.TOrder;
import com.fd.rookie.spring.boot.service.order.AbstractHandlerOrder;
import org.springframework.stereotype.Component;

@Component
@HandlerType("3")
public class PromotionHandler implements AbstractHandlerOrder {
    @Override
    public String orderHandler(TOrder tOrder) {
        return "处理促销订单";
    }
}
