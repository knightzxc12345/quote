package com.design.usecase.vendor;

import com.design.controller.vendor.request.VendorUpdateRequest;

import java.util.UUID;

public interface VendorUpdateUseCase {

    void update(VendorUpdateRequest request, UUID vendorUuid);

}
