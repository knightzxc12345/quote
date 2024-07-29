package com.design.controller.vendor_quote.response;

import java.util.List;
import java.util.UUID;

public record VendorQuoteFindResponse(

        String vendorName,

        String customerName,

        Integer status,

        List<VendorQuoteFindResponse.Item> items

) {

        public record Item(

                String itemName,

                String itemNo,

                String itemSpec,

                String itemUnit,

                Integer qty

        ){
        }

}
