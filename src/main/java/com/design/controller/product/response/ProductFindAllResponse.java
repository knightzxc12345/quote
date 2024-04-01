package com.design.controller.product.response;

import java.math.BigDecimal;

public record ProductFindAllResponse(

        String productUuid,

        String itemUuid,

        String specification,

        String unit,

        BigDecimal unitPrice,

        BigDecimal costPrice

) {
}
