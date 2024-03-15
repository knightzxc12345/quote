package com.quote.usecase.product;

import com.quote.controller.product.request.ProductUpdateRequest;

public interface ProductUpdateUseCase {

    void update(ProductUpdateRequest request, String productUuid);

}
