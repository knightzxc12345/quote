package com.design.usecase.login;

import com.design.controller.common.request.LoginRequest;
import com.design.controller.common.response.CommonLoginResponse;

public interface LoginUseCase {

    CommonLoginResponse login(final LoginRequest loginRequest);

}
