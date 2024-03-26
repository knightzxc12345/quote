package com.design.controller.quote.response;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record QuoteFindResponse(

        String quoteUUid,

        String userUuid,

        String customerUuid,

        String underTakerName,

        String underTakerTel,

        @NotNull(message = "產品清單")
        List<QuoteFindResponse.Product> products

) {

        public record Product(

                String vendorUuid,

                String itemUuid,

                String productUuid,

                Integer quantity,

                BigDecimal customUnitPrice

        ){
        }

}
