package com.design.controller.quote.response;

import java.util.List;

public record QuoteFindPageResponse(

        Integer pageTotal,

        Integer pageNow,

        Integer pageSize,

        List<QuoteFindAllResponse> responses

) {
}
