package com.quote.controller.quote.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record QuoteUpdateRequest(

        @NotBlank(message = "業務uuid不得為空")
        String userUuid,

        @NotBlank(message = "客戶uuid不得為空")
        String customerUuid,

        String handleStaffName,

        String handleStaffMobile,

        @NotNull(message = "產品清單")
        List<QuoteUpdateRequest.Product> products

) {

    public record Product(

            @NotBlank(message = "產品uuid不得為空")
            String productUuid,

            @NotNull(message = "產品數量")
            Integer quantity,

            @NotNull(message = "單價")
            BigDecimal unitPrice

    ){
    }

}
