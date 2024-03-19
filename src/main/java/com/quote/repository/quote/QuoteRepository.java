package com.quote.repository.quote;

import com.quote.entity.quote.QuoteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteRepository extends JpaRepository<QuoteEntity, Long> {

    QuoteEntity findByIsDeletedFalseAndUuid(String uuid);

    @Query(value =
            """
            SELECT
                q
            FROM 
                QuoteEntity q 
            WHERE 
                q.isDeleted = false
                AND (:userUuid IS NULL OR q.userUuid = :userUuid)
                AND 
                (
                    (:keyword IS NULL OR q.customerName LIKE CONCAT('%', :keyword, '%'))
                )
            ORDER BY 
                q.createTime
            """
    )
    List<QuoteEntity> findAll(
            @Param("userUuid") String userUuid,
            @Param("keyword") String keyword
    );

    @Query(value =
            """
            SELECT
                q
            FROM
                QuoteEntity q
            WHERE 
                q.isDeleted = false
                AND (:userUuid IS NULL OR q.userUuid = :userUuid)
                AND 
                (
                    (:keyword IS NULL OR q.customerName LIKE CONCAT('%', :keyword, '%'))
                )
            ORDER BY 
                q.createTime
            """
    )
    Page<QuoteEntity> findAllByPage(
            @Param("userUuid") String userUuid,
            @Param("keyword") String keyword,
            Pageable pageable
    );

}
