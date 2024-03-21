package com.design.usecase.customer;

import com.design.controller.common.response.CommonCustomerFindAllResponse;
import com.design.controller.customer.request.CustomerFindRequest;
import com.design.controller.customer.response.CustomerFindAllResponse;
import com.design.controller.customer.response.CustomerFindPageResponse;
import com.design.controller.customer.response.CustomerFindResponse;
import com.design.entity.customer.CustomerEntity;
import com.design.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerFindUseCaseImpl implements CustomerFindUseCase {

    private final CustomerService customerService;

    @Override
    public CustomerFindResponse findByUuid(String customerUuid) {
        CustomerEntity customerEntity = customerService.findByUuid(customerUuid);
        return format(customerEntity);
    }

    @Override
    public List<CustomerFindAllResponse> findAll(CustomerFindRequest request) {
        List<CustomerEntity> customerEntities = customerService.findAllLike(request.keyword());
        return format(customerEntities);
    }

    @Override
    public CustomerFindPageResponse findAllByPage(CustomerFindRequest request) {
        Integer page = request.page();
        Integer size = null == request.size() ? 10 : request.size();
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerEntity> customerEntityPage = customerService.findAllLikeByPage(request.keyword(), pageable);
        List<CustomerFindAllResponse> responses = format(customerEntityPage.getContent());
        return new CustomerFindPageResponse(
                customerEntityPage.getTotalPages(),
                customerEntityPage.getNumber(),
                customerEntityPage.getSize(),
                responses
        );
    }

    @Override
    public List<CommonCustomerFindAllResponse> findAllCommon() {
        List<CustomerEntity> customerEntities = customerService.findAll();
        return formatCommon(customerEntities);
    }

    private CustomerFindResponse format(CustomerEntity customerEntity){
        return new CustomerFindResponse(
                customerEntity.getUuid(),
                customerEntity.getName(),
                customerEntity.getAddress(),
                customerEntity.getVatNumber(),
                customerEntity.getDeputyManagerName(),
                customerEntity.getDeputyManagerMobile(),
                customerEntity.getDeputyManagerEmail(),
                customerEntity.getManagerName(),
                customerEntity.getManagerMobile(),
                customerEntity.getManagerEmail(),
                customerEntity.getGeneralAffairsManagerName(),
                customerEntity.getGeneralAffairsManagerMobile(),
                customerEntity.getGeneralAffairsManagerEmail()
        );
    }

    private List<CustomerFindAllResponse> format(List<CustomerEntity> customerEntities){
        List<CustomerFindAllResponse> responses = new ArrayList<>();
        if(null == customerEntities || customerEntities.isEmpty()){
            return responses;
        }
        for(CustomerEntity customerEntity : customerEntities){
            responses.add(new CustomerFindAllResponse(
                    customerEntity.getUuid(),
                    customerEntity.getName(),
                    customerEntity.getAddress(),
                    customerEntity.getVatNumber(),
                    customerEntity.getDeputyManagerName(),
                    customerEntity.getDeputyManagerMobile(),
                    customerEntity.getDeputyManagerEmail(),
                    customerEntity.getManagerName(),
                    customerEntity.getManagerMobile(),
                    customerEntity.getManagerEmail(),
                    customerEntity.getGeneralAffairsManagerName(),
                    customerEntity.getGeneralAffairsManagerMobile(),
                    customerEntity.getGeneralAffairsManagerEmail()
            ));
        }
        return responses;
    }

    private List<CommonCustomerFindAllResponse> formatCommon(List<CustomerEntity> customerEntities){
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
