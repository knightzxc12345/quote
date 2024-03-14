package com.quote.controller.customer.response;

public record CustomerFindAllResponse(

        String customerUuid,

        String name,

        String address

) {
}
