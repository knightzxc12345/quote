package com.design.usecase.product;

import com.design.controller.product.request.ProductCreateRequest;
import com.design.entity.product.ProductEntity;
import com.design.service.product.ProductService;
import com.design.utils.JwtUtil;
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
        productEntity.setItemUuid(request.itemUuid());
        productEntity.setNo(request.no());
        productEntity.setSpecification(request.specification());
        productEntity.setUnit(request.unit());
        productEntity.setUnitPrice(request.unitPrice());
        productEntity.setCostPrice(request.costPrice());
        productService.create(productEntity, JwtUtil.extractUsername());
    }

}
