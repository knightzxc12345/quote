package com.quote.usecase.customer;

import com.quote.controller.customer.request.CustomerCreateRequest;

public interface CustomerCreateUseCase {

    void create(CustomerCreateRequest request);

}
