package com.quote.usecase.product;

import com.quote.entity.product.ProductEntity;
import com.quote.service.product.ProductService;
import com.quote.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductDeleteUseCaseImpl implements ProductDeleteUseCase {

    private final ProductService productService;

    @Override
    public void delete(String productUuid) {
        ProductEntity productEntity = productService.findByUuid(productUuid);
        productService.delete(productEntity, JwtUtil.extractUsername());
    }

}
