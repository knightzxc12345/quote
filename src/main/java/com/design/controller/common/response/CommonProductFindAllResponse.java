package com.design.controller.common.response;

import java.math.BigDecimal;

public record CommonProductFindAllResponse(

        String productUuid,

        String vendorUuid,

        String itemUuid,

        String no,

        String name,

        String specification,

        String unit,

        BigDecimal unitPrice,

        BigDecimal costPrice

) {
}
