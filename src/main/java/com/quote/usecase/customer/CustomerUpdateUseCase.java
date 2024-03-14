package com.quote.usecase.customer;

import com.quote.controller.customer.request.CustomerUpdateRequest;

public interface CustomerUpdateUseCase {

    void update(CustomerUpdateRequest request, String customerUuid);

}
