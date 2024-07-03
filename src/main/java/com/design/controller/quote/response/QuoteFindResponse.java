package com.design.controller.quote.response;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record QuoteFindResponse(

        UUID quoteUUid,

        UUID userUuid,

        UUID customerUuid,

        String underTakerName,

        String underTakerTel,

        @NotNull(message = "產品清單")
        List<QuoteFindResponse.Product> products

) {

        public record Product(

                UUID itemUuid,

                UUID productUuid,

                Integer quantity,

                BigDecimal customUnitPrice

        ){
        }

}
