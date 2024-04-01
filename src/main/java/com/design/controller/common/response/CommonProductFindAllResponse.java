package com.design.controller.common.response;

import java.math.BigDecimal;

public record CommonProductFindAllResponse(

        String productUuid,

        String itemUuid,

        String itemNo,

        String specification,

        String unit,

        BigDecimal unitPrice,

        BigDecimal costPrice

) {
}
