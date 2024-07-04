package com.design.usecase.common;

import com.design.controller.common.response.CommonCustomerFindAllResponse;
import com.design.entity.customer.CustomerEntity;
import com.design.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonCustomerFindUseCaseImpl implements CommonCustomerFindUseCase {

    private final CustomerService customerService;

    @Override
    public List<CommonCustomerFindAllResponse> findAll() {
        List<CustomerEntity> customerEntities = customerService.findAll();
        return format(customerEntities);
    }

    private List<CommonCustomerFindAllResponse> format(List<CustomerEntity> customerEntities){
        List<CommonCustomerFindAllResponse> responses = new ArrayList<>();
        if(null == customerEntities || customerEntities.isEmpty()){
            return responses;
        }
        for(CustomerEntity customerEntity : customerEntities){
            responses.add(new CommonCustomerFindAllResponse(
                    customerEntity.getUuid(),
                    customerEntity.getName(),
                    customerEntity.getAddress()
            ));
        }
        return responses;
    }

}
