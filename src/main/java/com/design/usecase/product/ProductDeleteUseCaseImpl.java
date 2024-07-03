package com.design.usecase.product;

import com.design.entity.product.ProductEntity;
import com.design.service.product.ProductService;
import com.design.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductDeleteUseCaseImpl implements ProductDeleteUseCase {

    private final ProductService productService;

    @Override
    public void delete(UUID productUuid) {
        ProductEntity productEntity = productService.findByUuid(productUuid);
        productService.delete(productEntity, JwtUtil.extractUserUuid());
    }

}
