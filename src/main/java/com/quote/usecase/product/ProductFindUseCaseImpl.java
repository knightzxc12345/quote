package com.quote.usecase.product;

import com.quote.controller.product.request.ProductFindRequest;
import com.quote.controller.product.response.ProductFindAllResponse;
import com.quote.controller.product.response.ProductFindPageResponse;
import com.quote.controller.product.response.ProductFindResponse;
import com.quote.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductFindUseCaseImpl implements ProductFindUseCase {

    private final ProductService productService;

    @Override
    public ProductFindResponse findByUuid(String productUuid) {
        return null;
    }

    @Override
    public List<ProductFindAllResponse> findAll(ProductFindRequest request) {
        return null;
    }

    @Override
    public ProductFindPageResponse findAllByPage(ProductFindRequest request) {
        return null;
    }

}
