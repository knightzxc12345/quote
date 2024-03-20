package com.design.usecase.product;

import com.design.controller.product.request.ProductUpdateRequest;
import com.design.entity.product.ProductEntity;
import com.design.service.product.ProductService;
import com.design.utils.JwtUtil;
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
        productEntity.setItemUuid(request.itemUuid());
        productEntity.setSpecification(request.specification());
        productEntity.setUnit(request.unit());
        productEntity.setUnitPrice(request.unitPrice());
        productEntity.setCostPrice(request.costPrice());
        productService.update(productEntity, JwtUtil.extractUsername());
    }

}
