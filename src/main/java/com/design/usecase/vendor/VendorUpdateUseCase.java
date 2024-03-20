package com.design.usecase.vendor;

import com.design.controller.vendor.request.VendorUpdateRequest;

public interface VendorUpdateUseCase {

    void update(VendorUpdateRequest request, String vendorUuid);

}
