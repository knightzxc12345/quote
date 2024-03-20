package com.design.service.vendor;

import com.design.entity.vendor.VendorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VendorService {

    VendorEntity create(VendorEntity vendorEntity, String userUuid);

    void update(VendorEntity vendorEntity, String userUuid);

    void delete(VendorEntity vendorEntity, String userUuid);

    VendorEntity findByUuid(String vendorUuid);

    List<VendorEntity> findAll();

    List<VendorEntity> findAllLike(String keyword);

    Page<VendorEntity> findAllLikeByPage(String keyword, Pageable pageable);

}
