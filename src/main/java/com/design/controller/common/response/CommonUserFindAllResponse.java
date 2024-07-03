package com.design.controller.common.response;

import java.util.UUID;

public record CommonUserFindAllResponse(

        UUID userUuid,

        String name,

        String mobile

) {
}
