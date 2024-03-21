package com.design.controller.product.response;

import java.math.BigDecimal;

public record ProductFindAllResponse(

        String productUuid,

        String vendorUuid,

        String itemUuid,

        String no,

        String specification,

        String unit,

        BigDecimal unitPrice,

        BigDecimal costPrice

) {
}
