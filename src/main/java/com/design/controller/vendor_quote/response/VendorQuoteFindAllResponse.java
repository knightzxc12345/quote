package com.design.controller.vendor_quote.response;

import java.util.UUID;

public record VendorQuoteFindAllResponse(

        UUID vendorQuoteUuid,

        UUID vendorUuid,

        UUID customerUuid,

        String status,

        String createTime

) {
}
