package com.design.usecase.vendor_product;

import com.design.controller.vendor_product.request.VendorProductFindRequest;
import com.design.controller.vendor_product.response.VendorProductFindAllResponse;
import com.design.controller.vendor_product.response.VendorProductFindPageResponse;
import com.design.controller.vendor_product.response.VendorProductFindResponse;

import java.util.List;

public interface VendorProductFindUseCase {

    VendorProductFindResponse findByUuid(String vendorProductUuid);

    List<VendorProductFindAllResponse> findAll(VendorProductFindRequest request);

    VendorProductFindPageResponse findAllByPage(VendorProductFindRequest request);

}
