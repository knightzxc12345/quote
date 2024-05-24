package com.design.controller.vendor_quote.response;

import java.util.List;

public record VendorQuoteFindResponse(

        String vendorUuid,

        String customerUuid,

        Integer status,

        List<VendorQuoteFindResponse.Product> products

) {

        public record Product(

                String itemUuid,

                String productUuid,

                Integer qty

        ){
        }

}
