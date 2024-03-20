package com.design.controller.customer.response;

public record CustomerFindAllResponse(

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