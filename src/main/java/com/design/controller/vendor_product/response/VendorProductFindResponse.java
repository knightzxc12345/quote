package com.design.controller.vendor_product.response;

import java.util.UUID;

public record VendorProductFindResponse(

        UUID vendorUuid,

        String name,

        Long unitPrice

) {
}
