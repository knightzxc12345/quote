package com.design.controller.vendor_quote.response;

public record VendorQuoteFindAllResponse(

        String vendorQuoteUuid,

        String vendorUuid,

        String customerUuid,

        String status,

        String createTime

) {
}
