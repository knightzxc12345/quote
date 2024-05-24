package com.design.repository.vendor_quote;

import com.design.entity.vendor_quote.VendorQuoteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorQuoteRepository extends JpaRepository<VendorQuoteEntity, Long> {

    VendorQuoteEntity findByIsDeletedFalseAndUuid(String uuid);

    @Query(value =
            """
            SELECT
                v
            FROM 
                VendorQuoteEntity v 
            WHERE 
                v.isDeleted = false
                AND 
                (
                    (:vendorUuid IS NULL OR v.vendorUuid = :vendorUuid) OR
                    (:customerUuid IS NULL OR v.customerUuid = :customerUuid)
                )
            ORDER BY 
                v.createTime
            """
    )
    List<VendorQuoteEntity> findAll(
            @Param("vendorUuid") String vendorUuid,
            @Param("customerUuid") String customerUuid
    );

    @Query(value =
            """
            SELECT
                v
            FROM 
                VendorQuoteEntity v 
            WHERE 
                v.isDeleted = false
                AND 
                (
                    (:vendorUuid IS NULL OR v.vendorUuid = :vendorUuid) OR
                    (:customerUuid IS NULL OR v.customerUuid = :customerUuid)
                )
            """
    )
    Page<VendorQuoteEntity> findAllByPage(
            @Param("vendorUuid") String vendorUuid,
            @Param("customerUuid") String customerUuid,
            Pageable pageable
    );

}
