package com.design.service.product_vendor;

import com.design.entity.product_vendor.ProductVendorEntity;
import com.design.repository.product_vendor.ProductVendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductVendorServiceImpl implements ProductVendorService {

    private final ProductVendorRepository productVendorRepository;

    @Override
    public void createAll(List<ProductVendorEntity> productVendorEntities, UUID userUuid) {
        if(null == productVendorEntities || productVendorEntities.isEmpty()){
            return;
        }
        for(ProductVendorEntity productVendorEntity : productVendorEntities){
            productVendorEntity.setUuid(UUID.randomUUID());
            productVendorEntity.setIsDeleted(false);
            productVendorEntity.setCreateTime(Instant.now());
            productVendorEntity.setCreateUser(userUuid);
        }
        productVendorRepository.saveAll(productVendorEntities);
    }

    @Override
    public void deleteAll(List<ProductVendorEntity> productVendorEntities, UUID userUuid) {
        if(null == productVendorEntities || productVendorEntities.isEmpty()){
            return;
        }
        for(ProductVendorEntity productVendorEntity : productVendorEntities){
            productVendorEntity.setIsDeleted(false);
            productVendorEntity.setDeletedTime(Instant.now());
            productVendorEntity.setDeletedUser(userUuid);
        }
        productVendorRepository.saveAll(productVendorEntities);
    }

    @Override
    public List<ProductVendorEntity> findAll(UUID productUuid) {
        return productVendorRepository.findByIsDeletedFalseAndProductUuid(productUuid);
    }

    @Override
    public List<ProductVendorEntity> findAllProductUuidIn(List<UUID> productUuids) {
        return productVendorRepository.findByIsDeletedFalseAndProductUuidIn(productUuids);
    }

}
