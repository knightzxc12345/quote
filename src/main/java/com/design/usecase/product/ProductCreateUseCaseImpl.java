package com.design.usecase.product;

import com.design.controller.product.request.ProductCreateRequest;
import com.design.entity.product.ProductEntity;
import com.design.entity.product_vendor.ProductVendorEntity;
import com.design.service.product.ProductService;
import com.design.service.product_vendor.ProductVendorService;
import com.design.utils.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductCreateUseCaseImpl implements ProductCreateUseCase {

    private final ProductService productService;

    private final ProductVendorService productVendorService;

    @Override
    public void create(ProductCreateRequest request) {
        ProductEntity productEntity = initProduct(request);
        List<ProductVendorEntity> productVendorEntities = initProductVendors(request, productEntity);
        // 新增產品
        productService.create(productEntity, JwtUtil.extractUserUuid());
        // 新增產品廠商清單
        productVendorService.createAll(productVendorEntities, JwtUtil.extractUserUuid());
    }

    private ProductEntity initProduct(ProductCreateRequest request){
        ProductEntity productEntity = new ProductEntity();
        productEntity.setUuid(UUID.randomUUID());
        productEntity.setItemUuid(request.itemUuid());
        productEntity.setSpecification(request.specification());
        productEntity.setUnit(request.unit());
        productEntity.setUnitPrice(request.unitPrice());
        return productEntity;
    }

    private List<ProductVendorEntity> initProductVendors(ProductCreateRequest request, ProductEntity productEntity){
        List<ProductVendorEntity> productVendorEntities = new ArrayList<>();
        ProductVendorEntity productVendorEntity;
        for(UUID vendorUuid : request.vendors()){
            productVendorEntity = new ProductVendorEntity();
            productVendorEntity.setProductUuid(productEntity.getUuid());
            productVendorEntity.setVendorUuid(vendorUuid);
            productVendorEntities.add(productVendorEntity);
        }
        return productVendorEntities;
    }

}
