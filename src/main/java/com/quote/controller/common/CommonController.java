package com.quote.controller.common;

import com.quote.base.ResponseBody;
import com.quote.base.eunms.CommonEnum;
import com.quote.controller.common.request.LoginRequest;
import com.quote.controller.common.response.LoginResponse;
import com.quote.usecase.LoginUseCase;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/common")
@RestController
@RequiredArgsConstructor
@Validated
public class CommonController {

    final LoginUseCase loginUseCase;

    @PostMapping(
            value = "login/v1"
    )
    public ResponseBody login(
            @RequestBody @Validated @NotNull final LoginRequest request) {
        LoginResponse response = loginUseCase.login(request);
        return new ResponseBody(CommonEnum.C00001, response);
    }

}
