package com.design.controller.item.response;

import java.util.UUID;

public record ItemFindAllResponse(

        UUID itemUuid,

        String no,

        String name

) {
}
