package com.design.usecase.common;

import com.design.controller.common.response.CommonItemFindAllResponse;

import java.util.List;

public interface CommonItemFindUseCase {

    List<CommonItemFindAllResponse> findAll();

}
