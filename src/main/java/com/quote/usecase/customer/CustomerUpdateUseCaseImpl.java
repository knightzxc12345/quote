package com.quote.usecase.customer;

import com.quote.controller.customer.request.CustomerUpdateRequest;
import com.quote.entity.customer.CustomerEntity;
import com.quote.service.customer.CustomerService;
import com.quote.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerUpdateUseCaseImpl implements CustomerUpdateUseCase {

    private final CustomerService customerService;

    @Override
    public void update(CustomerUpdateRequest request, String customerUuid) {
        CustomerEntity customerEntity = customerService.findByUuid(customerUuid);
        customerEntity.setName(request.name());
        customerEntity.setAddress(request.address());
        customerService.update(customerEntity, JwtUtil.extractUsername());
    }

}
