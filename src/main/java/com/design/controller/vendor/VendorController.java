package com.design.controller.vendor;

import com.design.base.ResponseBody;
import com.design.base.eunms.CommonEnum;
import com.design.controller.vendor.request.VendorCreateRequest;
import com.design.controller.vendor.request.VendorFindRequest;
import com.design.controller.vendor.request.VendorUpdateRequest;
import com.design.controller.vendor.response.VendorFindAllResponse;
import com.design.controller.vendor.response.VendorFindPageResponse;
import com.design.controller.vendor.response.VendorFindResponse;
import com.design.usecase.vendor.VendorCreateUseCase;
import com.design.usecase.vendor.VendorDeleteUseCase;
import com.design.usecase.vendor.VendorFindUseCase;
import com.design.usecase.vendor.VendorUpdateUseCase;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/vendor")
@RestController
@RequiredArgsConstructor
@Validated
public class VendorController {

    private final VendorCreateUseCase vendorCreateUseCase;

    private final VendorUpdateUseCase vendorUpdateUseCase;

    private final VendorDeleteUseCase vendorDeleteUseCase;

    private final VendorFindUseCase vendorFindUseCase;

    @PostMapping(
            value = "v1"
    )
    public ResponseBody create(
            @RequestBody @Validated @NotNull final VendorCreateRequest request) {
        vendorCreateUseCase.create(request);
        return new ResponseBody(CommonEnum.C00003);
    }

    @PutMapping(
            value = "v1/{vendorUuid}"
    )
    public ResponseBody update(
            @PathVariable("vendorUuid") @NotNull final String vendorUuid,
            @RequestBody @Validated @NotNull final VendorUpdateRequest request) {
        vendorUpdateUseCase.update(request, vendorUuid);
        return new ResponseBody(CommonEnum.C00004);
    }

    @DeleteMapping(
            value = "v1/{vendorUuid}"
    )
    public ResponseBody delete(
            @PathVariable("vendorUuid") @NotNull final String vendorUuid) {
        vendorDeleteUseCase.delete(vendorUuid);
        return new ResponseBody(CommonEnum.C00005);
    }

    @GetMapping(
            value = "v1/{vendorUuid}"
    )
    public ResponseBody findByUuid(
            @PathVariable("vendorUuid") @NotNull final String vendorUuid) {
        VendorFindResponse response = vendorFindUseCase.findByUuid(vendorUuid);
        return new ResponseBody(CommonEnum.C00001, response);
    }

    @GetMapping(
            value = "v1"
    )
    public ResponseBody findAll(
            @Validated final VendorFindRequest request) {
        if(null == request.page()){
            List<VendorFindAllResponse> responses = vendorFindUseCase.findAll(request);
            return new ResponseBody(CommonEnum.C00002, responses);
        }
        VendorFindPageResponse response = vendorFindUseCase.findAllByPage(request);
        return new ResponseBody(CommonEnum.C00002, response);
    }

}
