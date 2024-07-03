package com.design.usecase.common;

import com.design.controller.common.response.CommonUserFindAllResponse;

import java.util.List;
import java.util.UUID;

public interface CommonUserFindUseCase {

    List<CommonUserFindAllResponse> findAllByRoleUuid(UUID roleUuid);

}
