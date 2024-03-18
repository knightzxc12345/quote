package com.quote.controller.vendor.request;

import jakarta.validation.constraints.NotBlank;

public record VendorCreateRequest(

        @NotBlank(message = "名稱不得為空")
        String name,

        String address,

        String mobile,

        String tel,

        String fax

) {
}
