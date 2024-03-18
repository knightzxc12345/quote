package com.quote.usecase.vendor;

import com.quote.controller.vendor.request.VendorFindRequest;
import com.quote.controller.vendor.response.VendorFindAllResponse;
import com.quote.controller.vendor.response.VendorFindPageResponse;
import com.quote.controller.vendor.response.VendorFindResponse;

import java.util.List;

public interface VendorFindUseCase {

    VendorFindResponse findByUuid(String vendorUuid);

    List<VendorFindAllResponse> findAll(VendorFindRequest request);

    VendorFindPageResponse findAllByPage(VendorFindRequest request);

}
