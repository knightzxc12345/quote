package com.quote.controller.customer.response;

import java.util.List;

public record CustomerFindPageResponse(

        Integer pageTotal,

        Integer pageNow,

        Integer pageSize,

        List<CustomerFindAllResponse> responses

) {
}
