package com.quote.usecase.login;

import com.quote.controller.common.request.LoginRequest;
import com.quote.controller.common.response.LoginResponse;

public interface LoginUseCase {

    LoginResponse login(final LoginRequest loginRequest);

}
