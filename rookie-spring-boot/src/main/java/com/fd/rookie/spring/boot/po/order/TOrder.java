package com.fd.rookie.spring.boot.po.order;

import com.fd.rookie.spring.boot.po.Po;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单实体类
 */
@Data
public class TOrder extends Po{
    private static final long serialVersionUID = 3103169686543308214L;

    private String code;

    private BigDecimal price;

    private String type;
}
