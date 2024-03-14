package com.quote.controller.customer.response;

public record CustomerFindResponse(

        String customerUuid,

        String name,

        String address

) {
}
