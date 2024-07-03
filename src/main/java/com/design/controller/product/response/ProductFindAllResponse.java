package com.design.controller.product.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProductFindAllResponse(

        UUID productUuid,

        UUID itemUuid,

        String itemNo,

        String specification,

        String unit,

        BigDecimal unitPrice,

        BigDecimal costPrice,

        List<UUID> vendorUuids

) {
}
