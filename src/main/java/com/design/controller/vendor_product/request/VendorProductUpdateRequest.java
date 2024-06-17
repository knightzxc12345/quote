package com.design.controller.vendor_product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VendorProductUpdateRequest(

        @NotBlank(message = "廠商uuid不得為空")
        String vendorUuid,

        @NotBlank(message = "名稱不得為空")
        String name,

        @NotNull(message = "單價不得為空")
        Long unitPrice

) {
}
