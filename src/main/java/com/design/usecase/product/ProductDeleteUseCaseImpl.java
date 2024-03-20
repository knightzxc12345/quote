package com.design.usecase.product;

import com.design.entity.product.ProductEntity;
import com.design.service.product.ProductService;
import com.design.utils.JwtUtil;
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
