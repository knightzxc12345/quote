package com.design.usecase.vendor;

import com.design.controller.vendor.request.VendorFindRequest;
import com.design.controller.vendor.response.VendorFindAllResponse;
import com.design.controller.vendor.response.VendorFindPageResponse;
import com.design.controller.vendor.response.VendorFindResponse;

import java.util.List;
import java.util.UUID;

public interface VendorFindUseCase {

    VendorFindResponse findByUuid(UUID vendorUuid);

    List<VendorFindAllResponse> findAll(VendorFindRequest request);

    VendorFindPageResponse findAllByPage(VendorFindRequest request);

}
