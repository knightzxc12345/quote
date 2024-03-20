package com.design.controller.product.response;

import java.math.BigDecimal;

public record ProductFindResponse(

        String productUuid,

        String vendorUuid,

        String no,

        String itemUuid,

        String specification,

        String unit,

        BigDecimal unitPrice,

        BigDecimal costPrice

) {
}
