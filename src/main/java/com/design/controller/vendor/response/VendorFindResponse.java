package com.design.controller.vendor.response;

public record VendorFindResponse(

        String vendorUuid,

        String name,

        String address,

        String mobile,

        String tel,

        String fax

) {
}