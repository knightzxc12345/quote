package com.design.usecase.product;

import com.design.controller.product.request.ProductUpdateRequest;
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

@Service
@RequiredArgsConstructor
@Transactional
public class ProductUpdateUseCaseImpl implements ProductUpdateUseCase {

    private final ProductService productService;

    private final ProductVendorService productVendorService;

    @Override
    public void update(ProductUpdateRequest request, String productUuid) {
        ProductEntity productEntity = productService.findByUuid(productUuid);
        productEntity.setItemUuid(request.itemUuid());
        productEntity.setSpecification(request.specification());
        productEntity.setUnit(request.unit());
        productEntity.setUnitPrice(request.unitPrice());
        productEntity.setCostPrice(request.costPrice());
        List<ProductVendorEntity> oldProductVendorEntities = productVendorService.findAll(productUuid);
        List<ProductVendorEntity> newProductVendorEntities = getProductVendors(request, productEntity);
        // 更新產品
        productService.update(productEntity, JwtUtil.extractUsername());
        // 刪除產品廠商清單
        productVendorService.deleteAll(oldProductVendorEntities, JwtUtil.extractUsername());
        // 新增產品廠商清單
        productVendorService.createAll(newProductVendorEntities, JwtUtil.extractUsername());
    }

    private List<ProductVendorEntity> getProductVendors(ProductUpdateRequest request, ProductEntity productEntity){
        List<ProductVendorEntity> productVendorEntities = new ArrayList<>();
        ProductVendorEntity productVendorEntity;
        for(String vendorUuid : request.vendors()){
            productVendorEntity = new ProductVendorEntity();
            productVendorEntity.setProductUuid(productEntity.getUuid());
            productVendorEntity.setVendorUuid(vendorUuid);
            productVendorEntities.add(productVendorEntity);
        }
        return productVendorEntities;
    }

}
