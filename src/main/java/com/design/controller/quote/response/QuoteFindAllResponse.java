package com.design.controller.quote.response;

import java.math.BigDecimal;

public record QuoteFindAllResponse(

        String quoteUuid,

        String userUuid,

        String customerUuid,

        BigDecimal totalAmount,

        BigDecimal customTotalAmount,

        BigDecimal costTotalAmount,

        String createTime,

        Integer status

) {
}
