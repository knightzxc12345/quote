package com.design.usecase.customer;

import com.design.controller.customer.request.CustomerFindRequest;
import com.design.controller.customer.response.CustomerFindAllResponse;
import com.design.controller.customer.response.CustomerFindPageResponse;
import com.design.controller.customer.response.CustomerFindResponse;

import java.util.List;

public interface CustomerFindUseCase {

    CustomerFindResponse findByUuid(String customerUuid);

    List<CustomerFindAllResponse> findAll(CustomerFindRequest request);

    CustomerFindPageResponse findAllByPage(CustomerFindRequest request);

}
