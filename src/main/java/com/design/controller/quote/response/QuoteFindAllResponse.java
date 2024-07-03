package com.design.controller.quote.response;

import java.math.BigDecimal;
import java.util.UUID;

public record QuoteFindAllResponse(

        UUID quoteUuid,

        UUID userUuid,

        UUID customerUuid,

        BigDecimal totalAmount,

        BigDecimal customTotalAmount,

        BigDecimal costTotalAmount,

        String createTime,

        Integer status

) {
}
