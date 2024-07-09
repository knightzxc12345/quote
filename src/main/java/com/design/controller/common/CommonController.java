package com.design.controller.common;

import com.design.base.Common;
import com.design.base.ResponseBody;
import com.design.base.eunms.CommonEnum;
import com.design.controller.common.request.LoginRequest;
import com.design.controller.common.response.*;
import com.design.usecase.common.*;
import com.design.usecase.login.CommonLoginUseCase;
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

    private final CommonLoginUseCase commonLoginUseCase;

    private final CommonUserFindUseCase commonUserFindUseCase;

    private final CommonCustomerFindUseCase commonCustomerFindUseCase;

    private final CommonVendorFindUseCase commonVendorFindUseCase;

    private final CommonVendorProductFindUseCase commonVendorProductFindUseCase;

    private final CommonItemFindUseCase commonItemFindUseCase;

    @PostMapping(
            value = "login/v1"
    )
    public ResponseBody login(
            @RequestBody @Validated @NotNull final LoginRequest request) {
        CommonLoginResponse response = commonLoginUseCase.login(request);
        return new ResponseBody(CommonEnum.C00001, response);
    }

    @GetMapping(
            value = "user/v1/business"
    )
    public ResponseBody findAllBusiness() {
        List<CommonUserFindAllResponse> responses = commonUserFindUseCase.findAllByRoleUuid(Common.BUSINESS);
        return new ResponseBody(CommonEnum.C00002, responses);
    }

    @GetMapping(
            value = "customer/v1"
    )
    public ResponseBody findAllCustomer() {
        List<CommonCustomerFindAllResponse> responses = commonCustomerFindUseCase.findAll();
        return new ResponseBody(CommonEnum.C00002, responses);
    }

    @GetMapping(
            value = "vendor/v1"
    )
    public ResponseBody findAllVendor() {
        List<CommonVendorFindAllResponse> responses = commonVendorFindUseCase.findAll();
        return new ResponseBody(CommonEnum.C00002, responses);
    }

    @GetMapping(
            value = "vendor-product/v1"
    )
    public ResponseBody findAllVendorProduct() {
        List<CommonVendorProductFindAllResponse> responses = commonVendorProductFindUseCase.findAll();
        return new ResponseBody(CommonEnum.C00002, responses);
    }

    @GetMapping(
            value = "item/v1"
    )
    public ResponseBody findAllItem() {
        List<CommonItemFindAllResponse> responses = commonItemFindUseCase.findAll();
        return new ResponseBody(CommonEnum.C00002, responses);
    }

}
