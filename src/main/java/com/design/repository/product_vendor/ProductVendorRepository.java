package com.design.repository.product_vendor;

import com.design.entity.product_vendor.ProductVendorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVendorRepository extends JpaRepository<ProductVendorEntity, Long> {

    List<ProductVendorEntity> findByIsDeletedFalseAndProductUuid(String productUuid);

}
