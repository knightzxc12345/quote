package com.design.service.customer;

import com.design.entity.customer.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {

    CustomerEntity create(CustomerEntity customerEntity, String userUuid);

    void update(CustomerEntity customerEntity, String userUuid);

    void delete(CustomerEntity customerEntity, String userUuid);

    CustomerEntity findByUuid(String customerUuid);

    List<CustomerEntity> findAllLike(String keyword);

    Page<CustomerEntity> findAllLikeByPage(String keyword, Pageable pageable);

}
