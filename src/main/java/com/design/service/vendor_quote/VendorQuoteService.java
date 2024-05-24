package com.design.service.vendor_quote;

import com.design.entity.vendor_quote.VendorQuoteEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VendorQuoteService {

    void createAll(List<VendorQuoteEntity> vendorQuoteEntities, String userUuid);

    void deleteAll(List<VendorQuoteEntity> vendorQuoteEntities, String userUuid);

    VendorQuoteEntity findByUuid(String vendorQuoteUuid);

    List<VendorQuoteEntity> findAll(
            String vendorUuid,
            String customerUuid
    );

    Page<VendorQuoteEntity> findAllByPage(
            String vendorUuid,
            String customerUuid,
            Integer page,
            Integer size
    );

}
