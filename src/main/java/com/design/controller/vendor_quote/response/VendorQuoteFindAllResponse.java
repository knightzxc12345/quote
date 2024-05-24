package com.design.controller.vendor_quote.response;

import java.math.BigDecimal;

public record VendorQuoteFindAllResponse(

        String vendorQuoteUuid,

        String vendorUuid,

        String customerUuid,

        BigDecimal amount,

        String status,

        String createTime

) {
}
