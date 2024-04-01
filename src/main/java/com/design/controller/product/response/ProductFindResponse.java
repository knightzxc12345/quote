package com.design.controller.product.response;

import java.math.BigDecimal;

public record ProductFindResponse(

        String productUuid,

        String itemUuid,

        String itemNo,

        String specification,

        String unit,

        BigDecimal unitPrice,

        BigDecimal costPrice

) {
}
