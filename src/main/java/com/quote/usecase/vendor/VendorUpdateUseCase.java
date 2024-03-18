package com.quote.usecase.vendor;

import com.quote.controller.vendor.request.VendorUpdateRequest;

public interface VendorUpdateUseCase {

    void update(VendorUpdateRequest request, String vendorUuid);

}
