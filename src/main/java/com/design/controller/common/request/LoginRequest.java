package com.design.controller.common.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "帳號不得為空")
        String account,

        @NotBlank(message = "密碼不得為空")
        String password

) {
}
