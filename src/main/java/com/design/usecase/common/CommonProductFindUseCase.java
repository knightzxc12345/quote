package com.design.usecase.common;

import com.design.controller.common.response.CommonProductFindAllResponse;

import java.util.List;

public interface CommonProductFindUseCase {

    List<CommonProductFindAllResponse> findAll();

}
