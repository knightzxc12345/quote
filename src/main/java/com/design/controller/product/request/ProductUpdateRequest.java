package com.design.controller.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProductUpdateRequest(

        @NotNull(message = "品項uuid不得為空")
        UUID itemUuid,

        @NotBlank(message = "規格不得為空")
        String specification,

        @NotBlank(message = "單位不得為空")
        String unit,

        @NotNull(message = "單位金額不得為空")
        BigDecimal unitPrice,

        @NotNull(message = "成本不得為空")
        BigDecimal costPrice,

        @NotNull(message = "廠商清單不得為空")
        List<UUID> vendors

) {
}
