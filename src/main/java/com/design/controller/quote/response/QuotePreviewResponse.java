package com.design.controller.quote.response;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record QuotePreviewResponse(

        String userName,

        String customerName,

        String customerAddress,

        String underTakerName,

        String underTakerTel,

        BigDecimal amount,

        BigDecimal tax,

        BigDecimal totalAmount,

        BigDecimal customAmount,

        BigDecimal customTax,

        BigDecimal customTotalAmount,

        BigDecimal costAmount,

        BigDecimal costTax,

        BigDecimal costTotalAmount,

        @NotNull(message = "產品清單")
        List<QuotePreviewResponse.Product> products

) {

        public record Product(

                String vendorName,

                String itemName,

                String no,

                String specification,

                String unit,

                Integer quantity,

                BigDecimal customUnitPrice,

                BigDecimal customAmount

        ){
        }

}
