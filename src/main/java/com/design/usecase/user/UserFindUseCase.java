package com.design.usecase.user;

import com.design.controller.common.response.CommonUserFindAllResponse;

import java.util.List;

public interface UserFindUseCase {

    List<CommonUserFindAllResponse> findAllByRoleUuid(String roleUuid);

}
