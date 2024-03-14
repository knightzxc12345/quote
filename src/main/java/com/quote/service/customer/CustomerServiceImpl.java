package com.quote.service.customer;

import com.quote.base.eunms.CustomerEnum;
import com.quote.entity.customer.CustomerEntity;
import com.quote.handler.BusinessException;
import com.quote.repository.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerEntity create(CustomerEntity customerEntity, String userUuid) {
        check(customerEntity);
        customerEntity.setUuid(UUID.randomUUID().toString());
        customerEntity.setCreateTime(Instant.now());
        customerEntity.setCreateUser(userUuid);
        customerEntity.setIsDeleted(false);
        return customerRepository.save(customerEntity);
    }

    private void check(CustomerEntity customerEntity){
        CustomerEntity isExists = customerRepository.findByIsDeletedFalseAndName(customerEntity.getName());
        if(null != isExists){
            throw new BusinessException(CustomerEnum.CU0001);
        }
    }

    @Override
    public void update(CustomerEntity customerEntity, String userUuid) {
        customerEntity.setModifiedTime(Instant.now());
        customerEntity.setModifiedUser(userUuid);
        customerRepository.save(customerEntity);
    }

    @Override
    public void delete(CustomerEntity customerEntity, String userUuid) {
        customerEntity.setIsDeleted(true);
        customerEntity.setDeletedTime(Instant.now());
        customerEntity.setDeletedUser(userUuid);
        customerRepository.save(customerEntity);
    }

    @Override
    public CustomerEntity findByUuid(String customerUuid) {
        return customerRepository.findByIsDeletedFalseAndUuid(customerUuid);
    }

    @Override
    public List<CustomerEntity> findAllLike(String keyword) {
        return customerRepository.findAll(keyword);
    }

    @Override
    public Page<CustomerEntity> findAllLikeByPage(String keyword, Pageable pageable) {
        return customerRepository.findAllByPage(keyword, pageable);
    }

}
