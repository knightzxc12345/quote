package com.design.usecase.customer;

import com.design.controller.customer.request.CustomerUpdateRequest;
import com.design.entity.customer.CustomerEntity;
import com.design.service.customer.CustomerService;
import com.design.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerUpdateUseCaseImpl implements CustomerUpdateUseCase {

    private final CustomerService customerService;

    @Override
    public void update(CustomerUpdateRequest request, UUID customerUuid) {
        CustomerEntity customerEntity = customerService.findByUuid(customerUuid);
        customerEntity = update(customerEntity, request);
        customerService.update(customerEntity, JwtUtil.extractUserUuid());
    }

    private CustomerEntity update(CustomerEntity customerEntity, CustomerUpdateRequest request){
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
        return customerEntity;
    }

}
