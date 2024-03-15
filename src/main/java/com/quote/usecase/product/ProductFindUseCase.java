package com.quote.usecase.product;

import com.quote.controller.product.request.ProductFindRequest;
import com.quote.controller.product.response.ProductFindAllResponse;
import com.quote.controller.product.response.ProductFindPageResponse;
import com.quote.controller.product.response.ProductFindResponse;

import java.util.List;

public interface ProductFindUseCase {

    ProductFindResponse findByUuid(String productUuid);

    List<ProductFindAllResponse> findAll(ProductFindRequest request);

    ProductFindPageResponse findAllByPage(ProductFindRequest request);

}
