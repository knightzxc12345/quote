package com.design.controller.vendor_product;

import com.design.base.ResponseBody;
import com.design.base.eunms.CommonEnum;
import com.design.controller.vendor_product.request.VendorProductCreateRequest;
import com.design.controller.vendor_product.request.VendorProductFindRequest;
import com.design.controller.vendor_product.request.VendorProductUpdateRequest;
import com.design.controller.vendor_product.response.VendorProductFindAllResponse;
import com.design.controller.vendor_product.response.VendorProductFindPageResponse;
import com.design.controller.vendor_product.response.VendorProductFindResponse;
import com.design.usecase.vendor_product.VendorProductCreateUseCase;
import com.design.usecase.vendor_product.VendorProductDeleteUseCase;
import com.design.usecase.vendor_product.VendorProductFindUseCase;
import com.design.usecase.vendor_product.VendorProductUpdateUseCase;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/vendor-product")
@RestController
@RequiredArgsConstructor
@Validated
public class VendorProductController {

    private final VendorProductCreateUseCase vendorProductCreateUseCase;

    private final VendorProductUpdateUseCase vendorProductUpdateUseCase;

    private final VendorProductDeleteUseCase vendorProductDeleteUseCase;

    private final VendorProductFindUseCase vendorProductFindUseCase;

    @PostMapping(
            value = "v1"
    )
    public ResponseBody create(
            @RequestBody @Validated @NotNull final VendorProductCreateRequest request) {
        vendorProductCreateUseCase.create(request);
        return new ResponseBody(CommonEnum.C00003);
    }

    @PutMapping(
            value = "v1/{vendorProductUuid}"
    )
    public ResponseBody update(
            @PathVariable("vendorProductUuid") @NotNull final String vendorProductUuid,
            @RequestBody @Validated @NotNull final VendorProductUpdateRequest request) {
        vendorProductUpdateUseCase.update(request, vendorProductUuid);
        return new ResponseBody(CommonEnum.C00004);
    }

    @DeleteMapping(
            value = "v1/{vendorProductUuid}"
    )
    public ResponseBody delete(
            @PathVariable("vendorProductUuid") @NotNull final String vendorProductUuid) {
        vendorProductDeleteUseCase.delete(vendorProductUuid);
        return new ResponseBody(CommonEnum.C00005);
    }

    @GetMapping(
            value = "v1/{vendorProductUuid}"
    )
    public ResponseBody findByUuid(
            @PathVariable("vendorProductUuid") @NotNull final String vendorProductUuid) {
        VendorProductFindResponse response = vendorProductFindUseCase.findByUuid(vendorProductUuid);
        return new ResponseBody(CommonEnum.C00001, response);
    }

    @GetMapping(
            value = "v1"
    )
    public ResponseBody findAll(
            @Validated final VendorProductFindRequest request) {
        if(null == request.page() || null == request.size()){
            List<VendorProductFindAllResponse> responses = vendorProductFindUseCase.findAll(request);
            return new ResponseBody(CommonEnum.C00002, responses);
        }
        VendorProductFindPageResponse response = vendorProductFindUseCase.findAllByPage(request);
        return new ResponseBody(CommonEnum.C00002, response);
    }

}
