package com.design.controller.common.response;

import java.util.UUID;

public record CommonCustomerFindAllResponse(

        UUID customerUuid,

        String name,

        String address

) {
}
