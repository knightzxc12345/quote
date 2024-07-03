package com.design.controller.common.response;

import java.math.BigDecimal;
import java.util.UUID;

public record CommonProductFindAllResponse(

        UUID productUuid,

        UUID itemUuid,

        String itemNo,

        String specification,

        String unit,

        BigDecimal unitPrice

) {
}
