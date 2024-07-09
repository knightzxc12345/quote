package com.design.controller.item.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ItemFindAllResponse(

        UUID itemUuid,

        String no,

        String name,

        String spec,

        List<ItemFindAllResponse.VendorProduct> vendorProducts

) {

        public record VendorProduct(

                UUID vendorUuid,

                String vendorName,

                UUID vendorProductUuid,

                String vendorProductName,

                Integer qty,

                BigDecimal unitPrice

        ){
        }

}
