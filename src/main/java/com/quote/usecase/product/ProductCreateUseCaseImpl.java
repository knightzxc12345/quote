package com.quote.usecase.product;

import com.quote.controller.product.request.ProductCreateRequest;
import com.quote.entity.product.ProductEntity;
import com.quote.service.product.ProductService;
import com.quote.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCreateUseCaseImpl implements ProductCreateUseCase {

    private final ProductService productService;

    @Override
    public void create(ProductCreateRequest request) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setVendorUuid(request.vendorUuid());
        productEntity.setNo(request.no());
        productEntity.setName(request.name());
        productEntity.setSpecification(request.specification());
        productEntity.setUnit(request.unit());
        productEntity.setUnitPrice(request.unitPrice());
        productEntity.setOriginPrice(request.originPrice());
        productService.create(productEntity, JwtUtil.extractUsername());
    }

}
