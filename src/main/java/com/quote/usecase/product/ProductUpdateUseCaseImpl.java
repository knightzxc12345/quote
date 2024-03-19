package com.quote.usecase.product;

import com.quote.controller.product.request.ProductUpdateRequest;
import com.quote.entity.product.ProductEntity;
import com.quote.service.product.ProductService;
import com.quote.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductUpdateUseCaseImpl implements ProductUpdateUseCase {

    private final ProductService productService;

    @Override
    public void update(ProductUpdateRequest request, String productUuid) {
        ProductEntity productEntity = productService.findByUuid(productUuid);
        productEntity.setVendorUuid(request.vendorUuid());
        productEntity.setNo(request.no());
        productEntity.setName(request.name());
        productEntity.setSpecification(request.specification());
        productEntity.setUnit(request.unit());
        productEntity.setUnitPrice(request.unitPrice());
        productEntity.setCostPrice(request.costPrice());
        productService.update(productEntity, JwtUtil.extractUsername());
    }

}
