package com.quote.controller.customer.request;

import jakarta.validation.constraints.NotBlank;

public record CustomerUpdateRequest(

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
