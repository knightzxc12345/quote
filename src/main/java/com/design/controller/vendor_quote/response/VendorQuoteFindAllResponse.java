package com.design.controller.vendor_quote.response;

import java.util.UUID;

public record VendorQuoteFindAllResponse(

        UUID vendorQuoteUuid,

        String vendorName,

        String customerName,

        String status,

        String createTime

) {
}
