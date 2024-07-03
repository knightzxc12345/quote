package com.design.controller.vendor_product.request;

import jakarta.validation.constraints.Min;

import java.util.UUID;

public record VendorProductFindRequest(

        @Min(value = 0, message = "頁數不得小於0")
        Integer page,

        @Min(value = 0, message = "每頁筆數不得小於0")
        Integer size,

        UUID vendorUuid,

        String keyword

) {
}
