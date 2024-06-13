package com.design.usecase.vendor_product;

import com.design.controller.vendor_product.request.VendorProductUpdateRequest;

public interface VendorProductUpdateUseCase {

    void update(VendorProductUpdateRequest request, String vendorProductUuid);

}
