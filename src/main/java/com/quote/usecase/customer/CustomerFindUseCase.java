package com.quote.usecase.customer;

import com.quote.controller.customer.request.CustomerFindRequest;
import com.quote.controller.customer.response.CustomerFindAllResponse;
import com.quote.controller.customer.response.CustomerFindPageResponse;
import com.quote.controller.customer.response.CustomerFindResponse;

import java.util.List;

public interface CustomerFindUseCase {

    CustomerFindResponse findByUuid(String customerUuid);

    List<CustomerFindAllResponse> findAll(CustomerFindRequest request);

    CustomerFindPageResponse findAllByPage(CustomerFindRequest request);

}
