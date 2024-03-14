package com.quote.usecase.customer;

import com.quote.entity.customer.CustomerEntity;
import com.quote.service.customer.CustomerService;
import com.quote.utils.JwtUtil;
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
