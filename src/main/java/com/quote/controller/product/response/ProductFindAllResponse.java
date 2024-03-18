package com.quote.controller.product.response;

import java.math.BigDecimal;

public record ProductFindAllResponse(

        String productUuid,

        String vendorUuid,

        String no,

        String name,

        String specification,

        String unit,

        BigDecimal unitPrice,

        BigDecimal originPrice

) {
}
