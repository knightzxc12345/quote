package com.design.repository.product;

import com.design.entity.product.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    ProductEntity findByItemUuidAndSpecification(UUID itemUuid, String specification);

    ProductEntity findByIsDeletedFalseAndUuid(UUID productUuid);

    List<ProductEntity> findByIsDeletedFalseOrderByItemUuidAscSpecificationAsc();

    List<ProductEntity> findByIsDeletedFalseAndUuidIn(List<UUID> productUuids);

    List<ProductEntity> findByIsDeletedFalseAndItemUuid(UUID itemUuid);

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
                    (:keyword IS NULL OR p.specification LIKE CONCAT('%', :keyword, '%'))
                )
            ORDER BY 
                p.itemUuid,
                p.specification
            """
    )
    List<ProductEntity> findAll(
            @Param("keyword") String keyword
    );

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
                    (:keyword IS NULL OR p.specification LIKE CONCAT('%', :keyword, '%'))
                )
            """
    )
    Page<ProductEntity> findAllByPage(
            @Param("keyword") String keyword,
            Pageable pageable
    );

}
