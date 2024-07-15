package com.design.controller.quote.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record QuoteCreateRequest(

        @NotBlank(message = "業務uuid不得為空")
        UUID userUuid,

        @NotBlank(message = "客戶uuid不得為空")
        UUID customerUuid,

        String underTakerName,

        String underTakerTel,

        @NotNull(message = "品項清單")
        List<QuoteCreateRequest.Item> items

) {

        public record Item(

                @NotNull(message = "品項uuid不得為空")
                UUID itemUuid,

                @NotNull(message = "產品數量")
                Integer quantity,

                @NotNull(message = "客製單價")
                BigDecimal customUnitPrice

        ){
        }

}
