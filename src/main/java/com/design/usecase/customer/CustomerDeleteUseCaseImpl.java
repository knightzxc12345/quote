package com.design.usecase.customer;

import com.design.entity.customer.CustomerEntity;
import com.design.service.customer.CustomerService;
import com.design.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerDeleteUseCaseImpl implements CustomerDeleteUseCase {

    private final CustomerService customerService;

    @Override
    public void delete(String customerUuid) {
        CustomerEntity customerEntity = customerService.findByUuid(customerUuid);
        customerService.delete(customerEntity, JwtUtil.extractUsername());
    }

}
