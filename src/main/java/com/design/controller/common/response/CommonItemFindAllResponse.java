package com.design.controller.common.response;

import java.math.BigDecimal;
import java.util.UUID;

public record CommonItemFindAllResponse(

        UUID itemUuid,

        String itemNo,

        String name,

        String spec,

        String unit,

        BigDecimal unitPrice,

        BigDecimal amount

) {
}
