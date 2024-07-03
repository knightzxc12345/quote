package com.design.service.vendor_quote_detail;

import com.design.entity.vendor_quote_detail.VendorQuoteDetailEntity;
import com.design.repository.vendor_quote_detail.VendorQuoteDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorQuoteDetailServiceImpl implements VendorQuoteDetailService {

    private final VendorQuoteDetailRepository vendorQuoteDetailRepository;

    @Override
    public void createAll(List<VendorQuoteDetailEntity> vendorQuoteDetailEntities, UUID userUuid) {
        if(null == vendorQuoteDetailEntities || vendorQuoteDetailEntities.isEmpty()){
            return;
        }
        for(VendorQuoteDetailEntity vendorQuoteDetailEntity : vendorQuoteDetailEntities){
            vendorQuoteDetailEntity.setIsDeleted(false);
            vendorQuoteDetailEntity.setUuid(UUID.randomUUID());
            vendorQuoteDetailEntity.setCreateTime(Instant.now());
            vendorQuoteDetailEntity.setCreateUser(userUuid);
        }
        vendorQuoteDetailRepository.saveAll(vendorQuoteDetailEntities);
    }

    @Override
    public void deleteAll(List<VendorQuoteDetailEntity> vendorQuoteDetailEntities, UUID userUuid) {
        if(null == vendorQuoteDetailEntities || vendorQuoteDetailEntities.isEmpty()){
            return;
        }
        for(VendorQuoteDetailEntity vendorQuoteDetailEntity : vendorQuoteDetailEntities){
            vendorQuoteDetailEntity.setIsDeleted(true);
            vendorQuoteDetailEntity.setDeletedTime(Instant.now());
            vendorQuoteDetailEntity.setDeletedUser(userUuid);
        }
        vendorQuoteDetailRepository.saveAll(vendorQuoteDetailEntities);
    }

    @Override
    public List<VendorQuoteDetailEntity> findAllByVendorQuoteUuid(UUID vendorQuoteUuid) {
        return vendorQuoteDetailRepository.findByIsDeletedFalseAndVendorQuoteUuidOrderByPkAsc(vendorQuoteUuid);
    }

}
