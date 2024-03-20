package com.design.usecase.vendor;

import com.design.controller.common.response.CommonVendorFindAllResponse;
import com.design.controller.vendor.request.VendorFindRequest;
import com.design.controller.vendor.response.VendorFindAllResponse;
import com.design.controller.vendor.response.VendorFindPageResponse;
import com.design.controller.vendor.response.VendorFindResponse;

import java.util.List;

public interface VendorFindUseCase {

    VendorFindResponse findByUuid(String vendorUuid);

    List<VendorFindAllResponse> findAll(VendorFindRequest request);

    VendorFindPageResponse findAllByPage(VendorFindRequest request);

    List<CommonVendorFindAllResponse> findAllCommon();

}
