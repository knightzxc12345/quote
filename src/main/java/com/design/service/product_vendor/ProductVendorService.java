package com.design.service.product_vendor;

import com.design.entity.product_vendor.ProductVendorEntity;

import java.util.List;
import java.util.UUID;

public interface ProductVendorService {

    void createAll(List<ProductVendorEntity> productVendorEntities, UUID userUuid);

    void deleteAll(List<ProductVendorEntity> productVendorEntities, UUID userUuid);

    List<ProductVendorEntity> findAll(UUID productUuid);

    List<ProductVendorEntity> findAllProductUuidIn(List<UUID> productUuids);

}
