package com.design.repository.product;

import com.design.entity.product.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    ProductEntity findByVendorUuidAndNameAndSpecification(String vendorUuid, String name, String specification);

    ProductEntity findByIsDeletedFalseAndUuid(String productUuid);

    List<ProductEntity> findByIsDeletedFalseOrderByNoAscNameAscSpecificationAsc();

    @Query(value =
            """
            SELECT
                p
            FROM 
                ProductEntity p
            WHERE 
                p.isDeleted = false
                AND 
                (
                    (:keyword IS NULL OR p.name LIKE CONCAT('%', :keyword, '%')) OR
                    (:keyword IS NULL OR p.specification LIKE CONCAT('%', :keyword, '%'))
                )
            ORDER BY 
                p.vendorUuid,
                p.no,
                p.name,
                p.specification
            """
    )
    List<ProductEntity> findAll(@Param("keyword") String keyword);

    @Query(value =
            """
            SELECT
                p
            FROM
                ProductEntity p 
            WHERE 
                p.isDeleted = false
                AND 
                (
                    (:keyword IS NULL OR p.name LIKE CONCAT('%', :keyword, '%')) OR
                    (:keyword IS NULL OR p.specification LIKE CONCAT('%', :keyword, '%'))
                )
            """
    )
    Page<ProductEntity> findAllByPage(@Param("keyword") String keyword, Pageable pageable);

}
