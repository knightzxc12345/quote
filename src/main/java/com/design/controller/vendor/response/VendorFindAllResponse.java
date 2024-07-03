package com.design.controller.vendor.response;

import java.util.UUID;

public record VendorFindAllResponse(

        UUID vendorUuid,

        String name,

        String address,

        String mobile,

        String tel,

        String fax

) {
}
