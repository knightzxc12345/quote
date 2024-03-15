package com.quote.usecase.customer;

import com.quote.controller.customer.request.CustomerCreateRequest;
import com.quote.entity.customer.CustomerEntity;
import com.quote.service.customer.CustomerService;
import com.quote.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerCreateUseCaseImpl implements CustomerCreateUseCase {

    private final CustomerService customerService;

    @Override
    public void create(CustomerCreateRequest request) {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setName(request.name());
        customerEntity.setAddress(request.address());
        customerEntity.setDeputyManagerName(request.deputyManagerName());
        customerEntity.setDeputyManagerMobile(request.deputyManagerMobile());
        customerEntity.setDeputyManagerEmail(request.deputyManagerEmail());
        customerEntity.setManagerName(request.managerName());
        customerEntity.setManagerMobile(request.managerMobile());
        customerEntity.setManagerEmail(request.managerEmail());
        customerEntity.setGeneralAffairsManagerName(request.generalAffairsManagerName());
        customerEntity.setGeneralAffairsManagerMobile(request.generalAffairsManagerMobile());
        customerEntity.setGeneralAffairsManagerEmail(request.generalAffairsManagerEmail());
        customerService.create(customerEntity, JwtUtil.extractUsername());
    }

}
