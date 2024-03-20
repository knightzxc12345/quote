package com.design.controller.vendor.request;

import jakarta.validation.constraints.NotBlank;

public record VendorUpdateRequest(

        @NotBlank(message = "名稱不得為空")
        String name,

        String address,

        String mobile,

        String tel,

        String fax

) {
}
