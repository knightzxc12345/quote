package com.design.usecase.customer;

import com.design.controller.customer.request.CustomerCreateRequest;
import com.design.entity.customer.CustomerEntity;
import com.design.service.customer.CustomerService;
import com.design.utils.JwtUtil;
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
        customerEntity.setVatNumber(request.vatNumber());
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
