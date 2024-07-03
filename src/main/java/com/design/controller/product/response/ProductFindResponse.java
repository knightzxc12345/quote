package com.design.controller.product.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductFindResponse(

        UUID productUuid,

        UUID itemUuid,

        String itemNo,

        String specification,

        String unit,

        BigDecimal unitPrice,

        BigDecimal costPrice

) {
}
