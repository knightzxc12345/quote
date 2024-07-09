package com.design.service.vendor_product;

import com.design.base.eunms.VendorProductEnum;
import com.design.entity.vendor_product.VendorProductEntity;
import com.design.handler.BusinessException;
import com.design.repository.vendor_product.VendorProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorProductServiceImpl implements VendorProductService {

    private final VendorProductRepository vendorProductRepository;

    @Override
    public VendorProductEntity create(VendorProductEntity vendorProductEntity, UUID userUuid) {
        VendorProductEntity isExists = vendorProductRepository.findByIsDeletedFalseAndVendorUuidAndName(
                vendorProductEntity.getVendorUuid(),
                vendorProductEntity.getName()
        );
        if(null != isExists){
            throw new BusinessException(VendorProductEnum.VE0001);
        }
        vendorProductEntity.setIsDeleted(false);
        vendorProductEntity.setUuid(UUID.randomUUID());
        vendorProductEntity.setCreateTime(Instant.now());
        vendorProductEntity.setCreateUser(userUuid);
        return vendorProductRepository.save(vendorProductEntity);
    }

    @Override
    public void update(VendorProductEntity vendorProductEntity, UUID userUuid) {
        VendorProductEntity isExists = vendorProductRepository.findByIsDeletedFalseAndVendorUuidAndName(
                vendorProductEntity.getVendorUuid(),
                vendorProductEntity.getName()
        );
        if(null != isExists && !vendorProductEntity.getUuid().equals(isExists.getUuid())){
            throw new BusinessException(VendorProductEnum.VE0001);
        }
        vendorProductEntity.setModifiedTime(Instant.now());
        vendorProductEntity.setModifiedUser(userUuid);
        vendorProductRepository.save(vendorProductEntity);
    }

    @Override
    public void delete(VendorProductEntity vendorProductEntity, UUID userUuid) {
        vendorProductEntity.setIsDeleted(true);
        vendorProductEntity.setDeletedTime(Instant.now());
        vendorProductEntity.setDeletedUser(userUuid);
        vendorProductRepository.save(vendorProductEntity);
    }

    @Override
    public VendorProductEntity findByUuid(UUID vendorProductUuid) {
        return vendorProductRepository.findByIsDeletedFalseAndUuid(vendorProductUuid);
    }

    @Override
    public List<VendorProductEntity> findAll() {
        return vendorProductRepository.findByIsDeletedFalse();
    }

    @Override
    public List<VendorProductEntity> findAllIn(List<UUID> vendorProductUuids) {
        return vendorProductRepository.findByIsDeletedFalseAndUuidInOrderByNameAsc(vendorProductUuids);
    }

    @Override
    public List<VendorProductEntity> findAllLike(
            String vendorUuid,
            String keyword) {
        return vendorProductRepository.findAll(
                vendorUuid,
                keyword
        );
    }

    @Override
    public Page<VendorProductEntity> findAllLikeByPage(
            String vendorUuid,
            String keyword,
            Integer page,
            Integer size) {
        Sort sort = Sort.by(
                new Sort.Order(Sort.Direction.ASC, "vendorUuid"),
                new Sort.Order(Sort.Direction.ASC, "name")
        );
        Pageable pageable = PageRequest.of(page, size, sort);
        return vendorProductRepository.findAllByPage(
                vendorUuid,
                keyword,
                pageable
        );
    }

}
