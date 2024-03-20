package com.design.controller.item.response;

import java.util.List;

public record ItemFindPageResponse(

        Integer pageTotal,

        Integer pageNow,

        Integer pageSize,

        List<ItemFindAllResponse> responses

) {
}
