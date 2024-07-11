package com.design.controller.item.response;

import java.util.List;
import java.util.UUID;

public record ItemFindResponse(

        UUID itemUuid,

        String no,

        String name,

        List<vendorProduct> vendorProducts

) {

        public record vendorProduct(

                UUID vendorUuid,

                UUID itemVendorProductUuid

        ){
        }

}
