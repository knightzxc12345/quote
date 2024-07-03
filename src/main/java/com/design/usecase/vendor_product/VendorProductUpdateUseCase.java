package com.design.usecase.vendor_product;

import com.design.controller.vendor_product.request.VendorProductUpdateRequest;

import java.util.UUID;

public interface VendorProductUpdateUseCase {

    void update(VendorProductUpdateRequest request, UUID vendorProductUuid);

}
