package com.design.usecase.common;

import com.design.controller.common.response.CommonVendorFindAllResponse;

import java.util.List;

public interface CommonVendorFindUseCase {

    List<CommonVendorFindAllResponse> findAll();

}
