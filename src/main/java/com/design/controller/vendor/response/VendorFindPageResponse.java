package com.design.controller.vendor.response;

import java.util.List;

public record VendorFindPageResponse(

        Integer pageTotal,

        Integer pageNow,

        Integer pageSize,

        List<VendorFindAllResponse> responses

) {
}
