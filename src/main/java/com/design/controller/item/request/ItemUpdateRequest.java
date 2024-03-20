package com.design.controller.item.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ItemUpdateRequest(

        @NotBlank(message = "廠商不得為空")
        String vendorUuid,

        @NotBlank(message = "名稱不得為空")
        String name

) {
}
