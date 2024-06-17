package com.design.controller.vendor_product.response;

public record VendorProductFindAllResponse(

        String vendorProductUuid,

        String vendorUuid,

        String name,

        Long unitPrice

) {
}
