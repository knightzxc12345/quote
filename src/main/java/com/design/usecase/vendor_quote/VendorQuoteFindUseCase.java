package com.design.usecase.vendor_quote;

import com.design.controller.vendor_quote.request.VendorQuoteFindRequest;
import com.design.controller.vendor_quote.response.VendorQuoteFindAllResponse;
import com.design.controller.vendor_quote.response.VendorQuoteFindPageResponse;
import com.design.controller.vendor_quote.response.VendorQuoteFindResponse;

import java.util.List;
import java.util.UUID;

public interface VendorQuoteFindUseCase {

    VendorQuoteFindResponse findByUuid(UUID vendorQuoteUuid);

    List<VendorQuoteFindAllResponse> findAll(VendorQuoteFindRequest request);

    VendorQuoteFindPageResponse findAllByPage(VendorQuoteFindRequest request);

}
