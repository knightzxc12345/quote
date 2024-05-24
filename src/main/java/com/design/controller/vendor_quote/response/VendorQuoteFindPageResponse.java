package com.design.controller.vendor_quote.response;

import java.util.List;

public record VendorQuoteFindPageResponse(

        Integer pageTotal,

        Integer pageNow,

        Integer pageSize,

        List<VendorQuoteFindAllResponse> responses

) {
}
