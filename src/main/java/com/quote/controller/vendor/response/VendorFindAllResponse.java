package com.quote.controller.vendor.response;

public record VendorFindAllResponse(

        String vendorUuid,

        String name,

        String address,

        String mobile,

        String tel,

        String fax

) {
}
