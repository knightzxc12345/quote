package com.design.usecase.common;

import com.design.controller.common.response.CommonCustomerFindAllResponse;

import java.util.List;

public interface CommonCustomerFindUseCase {

    List<CommonCustomerFindAllResponse> findAll();

}
