package com.quote.controller.customer.response;

public record CustomerFindResponse(

        String customerUuid,

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
