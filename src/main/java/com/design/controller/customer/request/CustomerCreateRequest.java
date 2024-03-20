package com.design.controller.customer.request;

import jakarta.validation.constraints.NotBlank;

public record CustomerCreateRequest(

        @NotBlank(message = "名稱不得為空")
        String name,

        String address,

        String vatNumber,

        String deputyManagerName,

        String deputyManagerMobile,

        String deputyManagerEmail,

        String managerName,

        String managerMobile,

        String managerEmail,

        String generalAffairsManagerName,

        String generalAffairsManagerMobile,

        String generalAffairsManagerEmail

) {
}
