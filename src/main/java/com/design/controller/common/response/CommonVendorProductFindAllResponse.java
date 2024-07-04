package com.design.controller.common.response;

import java.math.BigDecimal;
import java.util.UUID;

public record CommonVendorProductFindAllResponse(

        UUID vendorUuid,

        UUID vendorProductUuid,

        String name,

        BigDecimal unitPrice

) {
}
