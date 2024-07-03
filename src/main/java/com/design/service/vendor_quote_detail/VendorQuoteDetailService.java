package com.design.service.vendor_quote_detail;

import com.design.entity.vendor_quote_detail.VendorQuoteDetailEntity;

import java.util.List;
import java.util.UUID;

public interface VendorQuoteDetailService {

    void createAll(List<VendorQuoteDetailEntity> vendorQuoteDetailEntities, UUID userUuid);

    void deleteAll(List<VendorQuoteDetailEntity> vendorQuoteDetailEntities, UUID userUuid);

    List<VendorQuoteDetailEntity> findAllByVendorQuoteUuid(UUID vendorQuoteUuid);

}
