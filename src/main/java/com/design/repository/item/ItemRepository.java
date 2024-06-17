package com.design.repository.item;

import com.design.entity.item.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

    ItemEntity findByIsDeletedFalseAndNoAndName(String no, String name);

    ItemEntity findByIsDeletedFalseAndUuid(String itemUuid);

    List<ItemEntity> findByIsDeletedFalseOrderByNoAscNameAsc();

    List<ItemEntity> findByIsDeletedFalseAndUuidIn(List<String> itemUuids);

    @Query(value =
            """
            SELECT
                i
            FROM
                ItemEntity i
            WHERE 
                i.isDeleted = false
            ORDER BY 
                i.no,
                i.name
            """
    )
    List<ItemEntity> findAllCommon();

    @Query(value =
            """
            SELECT
                i
            FROM 
                ItemEntity i
            WHERE 
                i.isDeleted = false
                AND 
                (
                    (:keyword IS NULL OR i.name LIKE CONCAT('%', :keyword, '%'))
                )
            ORDER BY 
                i.no,
                i.name
            """
    )
    List<ItemEntity> findAll(
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
                AND 
                (
                    (:keyword IS NULL OR i.name LIKE CONCAT('%', :keyword, '%'))
                )
            """
    )
    Page<ItemEntity> findAllByPage(
            @Param("keyword") String keyword,
            Pageable pageable
    );

}
