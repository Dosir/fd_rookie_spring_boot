package com.fd.rookie.spring.boot.service.impl.order;

import com.fd.rookie.spring.boot.annotation.HandlerType;
import com.fd.rookie.spring.boot.po.order.TOrder;
import com.fd.rookie.spring.boot.service.order.AbstractHandlerOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class HandlerOrderContext {
    private final Map<String, AbstractHandlerOrder> handlerOrderMap = new ConcurrentHashMap<>();

    /**
     * 注入所有实现了AbstractHandlerOrder接口的Bean
     * @param handlerOrderList
     */
    @Autowired
    public HandlerOrderContext(List<AbstractHandlerOrder> handlerOrderList) {
        this.handlerOrderMap.clear();
        for (AbstractHandlerOrder handlerOrder : handlerOrderList) {
            handlerOrderMap.put(handlerOrder.getClass().getAnnotation(HandlerType.class).value(), handlerOrder);
        }
    }

    /**
     * 计算价格
     * @param tOrder
     * @return
     */
    public String orderHandler(TOrder tOrder) {
        return handlerOrderMap.get(tOrder.getType()).orderHandler(tOrder);
    }
}
