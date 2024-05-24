package com.design.service.vendor_quote;

import com.design.entity.enums.VendorQuoteStatus;
import com.design.entity.vendor_quote.VendorQuoteEntity;
import com.design.repository.vendor_quote.VendorQuoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorQuoteServiceImpl implements VendorQuoteService {

     private final VendorQuoteRepository vendorQuoteRepository;

     @Override
     public void createAll(List<VendorQuoteEntity> vendorQuoteEntities, String userUuid) {
          if(null == vendorQuoteEntities || vendorQuoteEntities.isEmpty()){
               return;
          }
          for(VendorQuoteEntity vendorQuoteEntity : vendorQuoteEntities){
               vendorQuoteEntity.setIsDeleted(false);
               vendorQuoteEntity.setCreateTime(Instant.now());
               vendorQuoteEntity.setCreateUser(userUuid);
               vendorQuoteEntity.setVendorQuoteStatus(VendorQuoteStatus.CREATE);
          }
          vendorQuoteRepository.saveAll(vendorQuoteEntities);
     }

     @Override
     public void deleteAll(List<VendorQuoteEntity> vendorQuoteEntities, String userUuid) {
          if(null == vendorQuoteEntities || vendorQuoteEntities.isEmpty()){
               return;
          }
          for(VendorQuoteEntity vendorQuoteEntity : vendorQuoteEntities){
               vendorQuoteEntity.setIsDeleted(true);
               vendorQuoteEntity.setDeletedTime(Instant.now());
               vendorQuoteEntity.setDeletedUser(userUuid);
          }
          vendorQuoteRepository.saveAll(vendorQuoteEntities);
     }

     @Override
     public VendorQuoteEntity findByUuid(String vendorQuoteUuid) {
          return vendorQuoteRepository.findByIsDeletedFalseAndUuid(vendorQuoteUuid);
     }

     @Override
     public List<VendorQuoteEntity> findAll(
             String vendorUuid,
             String customerUuid) {
          return vendorQuoteRepository.findAll(
                  vendorUuid,
                  customerUuid
          );
     }

     @Override
     public Page<VendorQuoteEntity> findAllByPage(
             String vendorUuid,
             String customerUuid,
             Integer page,
             Integer size) {
          Sort sort = Sort.by(
                  new Sort.Order(Sort.Direction.ASC, "createTime")
          );
          Pageable pageable = PageRequest.of(page, size, sort);
          return vendorQuoteRepository.findAllByPage(
                  vendorUuid,
                  customerUuid,
                  pageable
          );
     }

}
