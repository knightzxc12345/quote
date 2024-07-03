package com.design.controller.vendor_product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record VendorProductUpdateRequest(

        @NotNull(message = "廠商uuid不得為空")
        UUID vendorUuid,

        @NotBlank(message = "名稱不得為空")
        String name,

        @NotNull(message = "單價不得為空")
        Long unitPrice

) {
}
