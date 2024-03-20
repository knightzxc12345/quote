package com.design.controller.product;

import com.design.base.ResponseBody;
import com.design.base.eunms.CommonEnum;
import com.design.controller.product.request.ProductCreateRequest;
import com.design.controller.product.request.ProductFindRequest;
import com.design.controller.product.request.ProductUpdateRequest;
import com.design.controller.product.response.ProductFindAllResponse;
import com.design.controller.product.response.ProductFindPageResponse;
import com.design.controller.product.response.ProductFindResponse;
import com.design.usecase.product.ProductCreateUseCase;
import com.design.usecase.product.ProductDeleteUseCase;
import com.design.usecase.product.ProductFindUseCase;
import com.design.usecase.product.ProductUpdateUseCase;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/product")
@RestController
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductCreateUseCase productCreateUseCase;

    private final ProductUpdateUseCase productUpdateUseCase;

    private final ProductDeleteUseCase productDeleteUseCase;

    private final ProductFindUseCase productFindUseCase;

    @PostMapping(
            value = "v1"
    )
    public ResponseBody create(
            @RequestBody @Validated @NotNull final ProductCreateRequest request) {
        productCreateUseCase.create(request);
        return new ResponseBody(CommonEnum.C00003);
    }

    @PutMapping(
            value = "v1/{productUuid}"
    )
    public ResponseBody update(
            @PathVariable("productUuid") @NotNull final String productUuid,
            @RequestBody @Validated @NotNull final ProductUpdateRequest request) {
        productUpdateUseCase.update(request, productUuid);
        return new ResponseBody(CommonEnum.C00004);
    }

    @DeleteMapping(
            value = "v1/{productUuid}"
    )
    public ResponseBody delete(
            @PathVariable("productUuid") @NotNull final String productUuid) {
        productDeleteUseCase.delete(productUuid);
        return new ResponseBody(CommonEnum.C00005);
    }

    @GetMapping(
            value = "v1/{productUuid}"
    )
    public ResponseBody findByUuid(
            @PathVariable("productUuid") @NotNull final String productUuid) {
        ProductFindResponse response = productFindUseCase.findByUuid(productUuid);
        return new ResponseBody(CommonEnum.C00001, response);
    }

    @GetMapping(
            value = "v1"
    )
    public ResponseBody findAll(
            @Validated final ProductFindRequest request) {
        if(null == request.page()){
            List<ProductFindAllResponse> responses = productFindUseCase.findAll(request);
            return new ResponseBody(CommonEnum.C00002, responses);
        }
        ProductFindPageResponse response = productFindUseCase.findAllByPage(request);
        return new ResponseBody(CommonEnum.C00002, response);
    }

}
