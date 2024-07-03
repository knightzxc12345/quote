package com.design.controller.vendor_quote.request;

import jakarta.validation.constraints.Min;

import java.util.UUID;

public record VendorQuoteFindRequest(

        @Min(value = 0, message = "頁數不得小於0")
        Integer page,

        @Min(value = 0, message = "每頁筆數不得小於0")
        Integer size,

        UUID vendorUuid,

        UUID customerUuid

) {
}
