package com.design.usecase.common;

import com.design.controller.common.response.CommonVendorProductFindAllResponse;

import java.util.List;

public interface CommonVendorProductFindUseCase {

    List<CommonVendorProductFindAllResponse> findAll();

}
