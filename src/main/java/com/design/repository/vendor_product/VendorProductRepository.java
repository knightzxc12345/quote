package com.design.repository.vendor_product;

import com.design.entity.vendor.VendorEntity;
import com.design.entity.vendor_product.VendorProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorProductRepository extends JpaRepository<VendorProductEntity, Long> {

    VendorProductEntity findByIsDeletedFalseAndVendorUuidAndName(String vendorUuid, String name);

    VendorProductEntity findByIsDeletedFalseAndUuid(String vendorProductUuid);

    @Query(value =
            """
            SELECT
                v
            FROM
                VendorProductEntity v
            WHERE
                v.isDeleted = false
                AND (:vendorUuid IS NULL OR v.vendorUuid = :vendorUuid)
                AND
                (
                    (:keyword IS NULL OR v.name LIKE CONCAT('%', :keyword, '%'))
                )
            ORDER BY
                v.name
            """
    )
    List<VendorProductEntity> findAll(
            @Param("vendorUuid") String vendorUuid,
            @Param("keyword") String keyword
    );

    @Query(value =
            """
            SELECT
                v
            FROM
                VendorProductEntity v
            WHERE
                v.isDeleted = false
                AND (:vendorUuid IS NULL OR v.vendorUuid = :vendorUuid)
                AND
                (
                    (:keyword IS NULL OR v.name LIKE CONCAT('%', :keyword, '%'))
                )
            """
    )
    Page<VendorProductEntity> findAllByPage(
            @Param("vendorUuid") String vendorUuid,
            @Param("keyword") String keyword,
            Pageable pageable
    );

}
