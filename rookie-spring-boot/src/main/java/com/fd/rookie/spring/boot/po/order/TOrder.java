package com.fd.rookie.spring.boot.po.order;

import com.fd.rookie.spring.boot.common.validator.UpdateGroup;
import com.fd.rookie.spring.boot.po.Po;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 订单实体类
 */
@Data
public class TOrder extends Po{
    private static final long serialVersionUID = 3103169686543308214L;

    private String code;

    @NotNull(message = "price 不允许为空")
    @DecimalMin(value = "0.1", message = "价格不能低于 {value}")
    private BigDecimal price;

    @NotBlank(message = "类型 不允许为空", groups = UpdateGroup.class)
    private String type;
}
