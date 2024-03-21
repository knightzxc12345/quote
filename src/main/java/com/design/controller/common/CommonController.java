package com.design.controller.common;

import com.design.base.Common;
import com.design.base.ResponseBody;
import com.design.base.eunms.CommonEnum;
import com.design.controller.common.request.LoginRequest;
import com.design.controller.common.response.*;
import com.design.usecase.customer.CustomerFindUseCase;
import com.design.usecase.item.ItemFindUseCase;
import com.design.usecase.login.LoginUseCase;
import com.design.usecase.product.ProductFindUseCase;
import com.design.usecase.user.UserFindUseCase;
import com.design.usecase.vendor.VendorFindUseCase;
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

    private final CustomerFindUseCase customerFindUseCase;

    private final VendorFindUseCase vendorFindUseCase;

    private final ProductFindUseCase productFindUseCase;

    private final ItemFindUseCase itemFindUseCase;

    @PostMapping(
            value = "login/v1"
    )
    public ResponseBody login(
            @RequestBody @Validated @NotNull final LoginRequest request) {
        CommonLoginResponse response = loginUseCase.login(request);
        return new ResponseBody(CommonEnum.C00001, response);
    }

    @GetMapping(
            value = "user/v1/business"
    )
    public ResponseBody findAllBusiness() {
        List<CommonUserFindAllResponse> responses = userFindUseCase.findAllCommonByRoleUuid(Common.BUSINESS);
        return new ResponseBody(CommonEnum.C00002, responses);
    }

    @GetMapping(
            value = "customer/v1"
    )
    public ResponseBody findAllCustomer() {
        List<CommonCustomerFindAllResponse> responses = customerFindUseCase.findAllCommon();
        return new ResponseBody(CommonEnum.C00002, responses);
    }

    @GetMapping(
            value = "vendor/v1"
    )
    public ResponseBody findAllVendor() {
        List<CommonVendorFindAllResponse> responses = vendorFindUseCase.findAllCommon();
        return new ResponseBody(CommonEnum.C00002, responses);
    }

    @GetMapping(
            value = "product/v1"
    )
    public ResponseBody findAllProduct() {
        List<CommonProductFindAllResponse> responses = productFindUseCase.findAllCommon();
        return new ResponseBody(CommonEnum.C00002, responses);
    }

    @GetMapping(
            value = "item/v1"
    )
    public ResponseBody findAllItem() {
        List<CommonItemFindAllResponse> responses = itemFindUseCase.findAllCommon();
        return new ResponseBody(CommonEnum.C00002, responses);
    }

}
