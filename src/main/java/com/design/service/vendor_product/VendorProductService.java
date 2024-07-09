package com.design.service.vendor_product;

import com.design.entity.vendor_product.VendorProductEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface VendorProductService {

    VendorProductEntity create(VendorProductEntity vendorProductEntity, UUID userUuid);

    void update(VendorProductEntity vendorProductEntity, UUID userUuid);

    void delete(VendorProductEntity vendorProductEntity, UUID userUuid);

    VendorProductEntity findByUuid(UUID vendorProductUuid);

    List<VendorProductEntity> findAll();

    List<VendorProductEntity> findAllIn(List<UUID> vendorProductUuids);

    List<VendorProductEntity> findAllLike(
            String vendorUuid,
            String keyword
    );

    Page<VendorProductEntity> findAllLikeByPage(
            String vendorUuid,
            String keyword,
            Integer page,
            Integer size
    );

}
