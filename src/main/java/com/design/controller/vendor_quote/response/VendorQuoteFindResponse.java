package com.design.controller.vendor_quote.response;

import java.util.List;
import java.util.UUID;

public record VendorQuoteFindResponse(

        UUID vendorUuid,

        UUID customerUuid,

        Integer status,

        List<VendorQuoteFindResponse.Product> products

) {

        public record Product(

                UUID itemUuid,

                UUID productUuid,

                Integer qty

        ){
        }

}
