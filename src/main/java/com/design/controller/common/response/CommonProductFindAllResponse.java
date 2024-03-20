package com.design.controller.common.response;

import java.math.BigDecimal;

public record CommonProductFindAllResponse(

        String vendorUuid,

        String productUuid,

        String no,

        String name,

        String specification,

        String unit,

        BigDecimal unitPrice,

        BigDecimal costPrice

) {
}
