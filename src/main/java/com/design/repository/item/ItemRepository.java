package com.design.repository.item;

import com.design.entity.item.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

    ItemEntity findByIsDeletedFalseAndVendorUuidAndName(String vendorUuid, String name);

    ItemEntity findByIsDeletedFalseAndUuid(String itemUuid);

    List<ItemEntity> findAll();

    @Query(value =
            """
            SELECT
                i
            FROM 
                ItemEntity i
            WHERE 
                i.isDeleted = false
                AND (:vendorUuid IS NULL OR i.vendorUuid = :vendorUuid)
                AND 
                (
                    (:keyword IS NULL OR i.name LIKE CONCAT('%', :keyword, '%'))
                )
            ORDER BY 
                i.vendorUuid,
                i.name
            """
    )
    List<ItemEntity> findAll(
            @Param("vendorUuid") String vendorUuid,
            @Param("keyword") String keyword
    );

    @Query(value =
            """
            SELECT
                i
            FROM
                ItemEntity i 
            WHERE 
                i.isDeleted = false
                AND (:vendorUuid IS NULL OR i.vendorUuid = :vendorUuid)
                AND 
                (
                    (:keyword IS NULL OR i.name LIKE CONCAT('%', :keyword, '%'))
                )
            """
    )
    Page<ItemEntity> findAllByPage(
            @Param("vendorUuid") String vendorUuid,
            @Param("keyword") String keyword,
            Pageable pageable
    );

}
