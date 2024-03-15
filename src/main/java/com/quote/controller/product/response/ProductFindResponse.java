package com.quote.controller.product.response;

import java.math.BigDecimal;

public record ProductFindResponse(

        String productUUid,

        String no,

        String name,

        String specification,

        String unit,

        BigDecimal unitPrice,

        BigDecimal originPrice

) {
}
