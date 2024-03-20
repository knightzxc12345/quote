package com.design.usecase.customer;

import com.design.controller.customer.request.CustomerUpdateRequest;

public interface CustomerUpdateUseCase {

    void update(CustomerUpdateRequest request, String customerUuid);

}
