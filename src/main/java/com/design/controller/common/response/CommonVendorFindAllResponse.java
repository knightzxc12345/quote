package com.design.controller.common.response;

import java.util.UUID;

public record CommonVendorFindAllResponse(

        UUID vendorUuid,

        String name

) {
}
