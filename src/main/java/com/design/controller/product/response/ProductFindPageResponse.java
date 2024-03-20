package com.design.controller.product.response;

import java.util.List;

public record ProductFindPageResponse(

        Integer pageTotal,

        Integer pageNow,

        Integer pageSize,

        List<ProductFindAllResponse> responses

) {
}
