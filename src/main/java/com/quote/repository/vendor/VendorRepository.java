package com.quote.repository.vendor;

import com.quote.entity.vendor.VendorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorRepository extends JpaRepository<VendorEntity, Long> {

    VendorEntity findByIsDeletedFalseAndName(String name);

    VendorEntity findByIsDeletedFalseAndUuid(String uuid);

    List<VendorEntity> findByIsDeletedFalseOrderByNameAsc();

    @Query(value =
            """
            SELECT
                v
            FROM 
                VendorEntity v 
            WHERE 
                v.isDeleted = false
                AND 
                (
                    (:keyword IS NULL OR v.name LIKE CONCAT('%', :keyword, '%'))
                )
            ORDER BY 
                v.name
            """
    )
    List<VendorEntity> findAll(@Param("keyword") String keyword);

    @Query(value =
            """
            SELECT
                v
            FROM
                VendorEntity v 
            WHERE 
                v.isDeleted = false
                AND 
                (
                    (:keyword IS NULL OR v.name LIKE CONCAT('%', :keyword, '%'))
                )
            ORDER BY 
                v.name
            """
    )
    Page<VendorEntity> findAllByPage(@Param("keyword") String keyword, Pageable pageable);

}
