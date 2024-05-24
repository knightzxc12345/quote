package com.design.service.vendor_quote_detail;

import com.design.entity.vendor_quote_detail.VendorQuoteDetailEntity;

import java.util.List;

public interface VendorQuoteDetailService {

    void createAll(List<VendorQuoteDetailEntity> vendorQuoteDetailEntities, String userUuid);

    void deleteAll(List<VendorQuoteDetailEntity> vendorQuoteDetailEntities, String userUuid);

    List<VendorQuoteDetailEntity> findAllByVendorQuoteUuid(String vendorQuoteUuid);

}
