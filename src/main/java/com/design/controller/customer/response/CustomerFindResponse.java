package com.design.controller.customer.response;

import java.util.UUID;

public record CustomerFindResponse(

        UUID customerUuid,

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
