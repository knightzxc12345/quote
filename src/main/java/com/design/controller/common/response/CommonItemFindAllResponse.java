package com.design.controller.common.response;

import java.util.UUID;

public record CommonItemFindAllResponse(

        UUID itemUuid,

        String itemNo,

        String name

) {
}
