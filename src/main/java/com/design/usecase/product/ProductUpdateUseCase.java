package com.design.usecase.product;

import com.design.controller.product.request.ProductUpdateRequest;

import java.util.UUID;

public interface ProductUpdateUseCase {

    void update(ProductUpdateRequest request, UUID productUuid);

}
