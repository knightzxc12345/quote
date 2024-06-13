package com.design.service.vendor_product;

import com.design.entity.vendor_product.VendorProductEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VendorProductService {

    VendorProductEntity create(VendorProductEntity vendorProductEntity, String userUuid);

    void update(VendorProductEntity vendorProductEntity, String userUuid);

    void delete(VendorProductEntity vendorProductEntity, String userUuid);

    VendorProductEntity findByUuid(String vendorProductUuid);

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
