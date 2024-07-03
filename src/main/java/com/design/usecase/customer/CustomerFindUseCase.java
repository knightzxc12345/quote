package com.design.usecase.customer;

import com.design.controller.customer.request.CustomerFindRequest;
import com.design.controller.customer.response.CustomerFindAllResponse;
import com.design.controller.customer.response.CustomerFindPageResponse;
import com.design.controller.customer.response.CustomerFindResponse;

import java.util.List;
import java.util.UUID;

public interface CustomerFindUseCase {

    CustomerFindResponse findByUuid(UUID customerUuid);

    List<CustomerFindAllResponse> findAll(CustomerFindRequest request);

    CustomerFindPageResponse findAllByPage(CustomerFindRequest request);

}
