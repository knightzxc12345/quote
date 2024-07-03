package com.design.service.vendor;

import com.design.entity.vendor.VendorEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface VendorService {

    VendorEntity create(VendorEntity vendorEntity, UUID userUuid);

    void update(VendorEntity vendorEntity, UUID userUuid);

    void delete(VendorEntity vendorEntity, UUID userUuid);

    VendorEntity findByUuid(UUID vendorUuid);

    List<VendorEntity> findAll();

    List<VendorEntity> findAllVendorUuidIn(List<UUID> vendorUuids);

    List<VendorEntity> findAllLike(String keyword);

    Page<VendorEntity> findAllLikeByPage(
            String keyword,
            Integer page,
            Integer size
    );

}
