package com.design.controller.product.response;

import java.math.BigDecimal;
import java.util.List;

public record ProductFindAllResponse(

        String productUuid,

        String itemUuid,

        String itemNo,

        String specification,

        String unit,

        BigDecimal unitPrice,

        BigDecimal costPrice,

        List<String> vendorUuids

) {
}
