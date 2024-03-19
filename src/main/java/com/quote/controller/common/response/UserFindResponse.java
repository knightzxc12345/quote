package com.quote.controller.common.response;

public record UserFindResponse(

        String userUuid,

        String name,

        String mobile

) {
}
