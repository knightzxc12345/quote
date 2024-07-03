package com.design.repository.product_vendor;

import com.design.entity.product_vendor.ProductVendorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductVendorRepository extends JpaRepository<ProductVendorEntity, Long> {

    List<ProductVendorEntity> findByIsDeletedFalseAndProductUuid(UUID productUuid);

    List<ProductVendorEntity> findByIsDeletedFalseAndProductUuidIn(List<UUID> productUuids);

}
