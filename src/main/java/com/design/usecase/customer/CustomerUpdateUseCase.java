package com.design.usecase.customer;

import com.design.controller.customer.request.CustomerUpdateRequest;

import java.util.UUID;

public interface CustomerUpdateUseCase {

    void update(CustomerUpdateRequest request, UUID customerUuid);

}
