package com.design.service.vendor_quote;

import com.design.entity.vendor_quote.VendorQuoteEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface VendorQuoteService {

    void createAll(List<VendorQuoteEntity> vendorQuoteEntities, UUID userUuid);

    void deleteAll(List<VendorQuoteEntity> vendorQuoteEntities, UUID userUuid);

    VendorQuoteEntity findByUuid(UUID vendorQuoteUuid);

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
