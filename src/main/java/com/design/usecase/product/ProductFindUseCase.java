package com.design.usecase.product;

import com.design.controller.product.request.ProductFindRequest;
import com.design.controller.product.response.ProductFindAllResponse;
import com.design.controller.product.response.ProductFindPageResponse;
import com.design.controller.product.response.ProductFindResponse;

import java.util.List;
import java.util.UUID;

public interface ProductFindUseCase {

    ProductFindResponse findByUuid(UUID productUuid);

    List<ProductFindAllResponse> findAll(ProductFindRequest request);

    ProductFindPageResponse findAllByPage(ProductFindRequest request);

}
