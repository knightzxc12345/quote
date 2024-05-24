package com.design.service.product_vendor;

import com.design.entity.product_vendor.ProductVendorEntity;

import java.util.List;

public interface ProductVendorService {

    void createAll(List<ProductVendorEntity> productVendorEntities, String userUuid);

    void deleteAll(List<ProductVendorEntity> productVendorEntities, String userUuid);

    List<ProductVendorEntity> findAll(String productUuid);

    List<ProductVendorEntity> findAllProductUuidIn(List<String> productUuids);

}
