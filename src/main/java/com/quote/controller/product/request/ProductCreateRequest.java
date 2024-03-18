package com.quote.controller.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductCreateRequest(

        @NotBlank(message = "廠商不得為空")
        String vendorUuid,

        @NotBlank(message = "編號不得為空")
        String no,

        @NotBlank(message = "名稱不得為空")
        String name,

        @NotBlank(message = "規格不得為空")
        String specification,

        @NotBlank(message = "單位不得為空")
        String unit,

        @NotNull(message = "單位金額不得為空")
        BigDecimal unitPrice,

        @NotNull(message = "成本不得為空")
        BigDecimal originPrice

) {
}
