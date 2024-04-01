package com.design.controller.item.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ItemCreateRequest(

        @NotBlank(message = "編號不得為空")
        String no,

        @NotBlank(message = "名稱不得為空")
        String name

) {
}
