package com.design.controller.vendor_product.response;

import java.util.UUID;

public record VendorProductFindAllResponse(

        UUID vendorProductUuid,

        UUID vendorUuid,

        String name,

        Long unitPrice

) {
}
