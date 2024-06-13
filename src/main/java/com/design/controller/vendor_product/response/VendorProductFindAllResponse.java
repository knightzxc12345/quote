package com.design.controller.vendor_product.response;

public record VendorProductFindAllResponse(

        String vendorUuid,

        String name,

        Long unitPrice

) {
}
