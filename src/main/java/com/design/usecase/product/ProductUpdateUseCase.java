package com.design.usecase.product;

import com.design.controller.product.request.ProductUpdateRequest;

public interface ProductUpdateUseCase {

    void update(ProductUpdateRequest request, String productUuid);

}
