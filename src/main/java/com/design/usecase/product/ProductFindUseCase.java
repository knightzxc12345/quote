package com.design.usecase.product;

import com.design.controller.common.response.CommonProductFindAllResponse;
import com.design.controller.product.request.ProductFindRequest;
import com.design.controller.product.response.ProductFindAllResponse;
import com.design.controller.product.response.ProductFindPageResponse;
import com.design.controller.product.response.ProductFindResponse;

import java.util.List;

public interface ProductFindUseCase {

    ProductFindResponse findByUuid(String productUuid);

    List<ProductFindAllResponse> findAll(ProductFindRequest request);

    ProductFindPageResponse findAllByPage(ProductFindRequest request);

    List<CommonProductFindAllResponse> findAllCommon();

}
