package com.design.controller.item.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ItemCreateRequest(

        @NotBlank(message = "編號不得為空")
        String no,

        @NotBlank(message = "名稱不得為空")
        String name,

        @NotNull(message = "廠商產品uuid清單不得為空")
        List<UUID> vendorProductUuid

) {
}
