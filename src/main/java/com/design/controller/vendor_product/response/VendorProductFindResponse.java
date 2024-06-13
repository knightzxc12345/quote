package com.design.controller.vendor_product.response;

public record VendorProductFindResponse(

        String vendorUuid,

        String name,

        Long unitPrice

) {
}
