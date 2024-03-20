package com.design.repository.customer;

import com.design.entity.customer.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    CustomerEntity findByIsDeletedFalseAndName(String name);

    CustomerEntity findByIsDeletedFalseAndUuid(String customerUuid);

    @Query(value =
            """
            SELECT
                c
            FROM 
                CustomerEntity c 
            WHERE 
                c.isDeleted = false
                AND 
                (
                    (:keyword IS NULL OR c.name LIKE CONCAT('%', :keyword, '%'))
                )
            ORDER BY 
                c.name
            """
    )
    List<CustomerEntity> findAll(@Param("keyword") String keyword);

    @Query(value =
            """
            SELECT
                c
            FROM
                CustomerEntity c 
            WHERE 
                c.isDeleted = false
                AND 
                (
                    (:keyword IS NULL OR c.name LIKE CONCAT('%', :keyword, '%'))
                )
            ORDER BY 
                c.name
            """
    )
    Page<CustomerEntity> findAllByPage(@Param("keyword") String keyword, Pageable pageable);

}
