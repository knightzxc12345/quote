package com.design.controller.vendor_product.response;

import java.util.List;

public record VendorProductFindPageResponse(

        Integer pageTotal,

        Integer pageNow,

        Integer pageSize,

        List<VendorProductFindAllResponse> responses

) {
}
