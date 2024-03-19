package com.quote.controller.common;

import com.quote.base.ResponseBody;
import com.quote.base.eunms.CommonEnum;
import com.quote.controller.common.request.LoginRequest;
import com.quote.controller.common.response.LoginResponse;
import com.quote.controller.common.response.UserFindResponse;
import com.quote.usecase.login.LoginUseCase;
import com.quote.usecase.user.UserFindUseCase;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/common")
@RestController
@RequiredArgsConstructor
@Validated
public class CommonController {

    private final LoginUseCase loginUseCase;

    private final UserFindUseCase userFindUseCase;

    @PostMapping(
            value = "login/v1"
    )
    public ResponseBody login(
            @RequestBody @Validated @NotNull final LoginRequest request) {
        LoginResponse response = loginUseCase.login(request);
        return new ResponseBody(CommonEnum.C00001, response);
    }

    @GetMapping(
            value = "user/v1/{roleUuid}"
    )
    public ResponseBody findAllUser(
            @PathVariable("roleUuid") @NotNull final String roleUuid) {
        List<UserFindResponse> responses = userFindUseCase.findAllByRoleUuid(roleUuid);
        return new ResponseBody(CommonEnum.C00002, responses);
    }

}
