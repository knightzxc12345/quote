package com.design.service.customer;

import com.design.entity.customer.CustomerEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    CustomerEntity create(CustomerEntity customerEntity, UUID userUuid);

    void update(CustomerEntity customerEntity, UUID userUuid);

    void delete(CustomerEntity customerEntity, UUID userUuid);

    CustomerEntity findByUuid(UUID customerUuid);

    List<CustomerEntity> findAll();

    List<CustomerEntity> findAllCustomerUuidIn(List<UUID> customerUuids);

    List<CustomerEntity> findAllLike(String keyword);

    Page<CustomerEntity> findAllLikeByPage(
            String keyword,
            Integer page,
            Integer size
    );

}
