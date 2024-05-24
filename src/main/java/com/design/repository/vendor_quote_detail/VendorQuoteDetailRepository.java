package com.design.repository.vendor_quote_detail;

import com.design.entity.vendor_quote_detail.VendorQuoteDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorQuoteDetailRepository extends JpaRepository<VendorQuoteDetailEntity, Long> {

    List<VendorQuoteDetailEntity> findByIsDeletedFalseAndVendorQuoteUuidOrderByPkAsc(String vendorQuoteUuid);

}
