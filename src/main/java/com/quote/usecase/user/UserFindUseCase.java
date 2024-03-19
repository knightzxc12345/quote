package com.quote.usecase.user;

import com.quote.controller.common.response.UserFindResponse;

import java.util.List;

public interface UserFindUseCase {

    List<UserFindResponse> findAllByRoleUuid(String roleUuid);

}
