package com.design.usecase.product;

import com.design.controller.product.request.ProductFindRequest;
import com.design.controller.product.response.ProductFindAllResponse;
import com.design.controller.product.response.ProductFindPageResponse;
import com.design.controller.product.response.ProductFindResponse;
import com.design.entity.item.ItemEntity;
import com.design.entity.product.ProductEntity;
import com.design.entity.product_vendor.ProductVendorEntity;
import com.design.service.item.ItemService;
import com.design.service.product.ProductService;
import com.design.service.product_vendor.ProductVendorService;
import com.design.utils.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductFindUseCaseImpl implements ProductFindUseCase {

    private final ProductService productService;

    private final ItemService itemService;

    private final ProductVendorService productVendorService;

    @Override
    public ProductFindResponse findByUuid(UUID productUuid) {
        ProductEntity productEntity = productService.findByUuid(productUuid);
        return format(productEntity);
    }

    @Override
    public List<ProductFindAllResponse> findAll(ProductFindRequest request) {
        List<ProductEntity> productEntities = productService.findAllLike(
                request.keyword()
        );
        return format(productEntities);
    }

    @Override
    public ProductFindPageResponse findAllByPage(ProductFindRequest request) {
        Page<ProductEntity> productEntityPage = productService.findAllLikeByPage(
                request.keyword(),
                request.page(),
                request.size()
        );
        List<ProductFindAllResponse> responses = format(productEntityPage.getContent());
        return new ProductFindPageResponse(
                productEntityPage.getTotalPages(),
                productEntityPage.getNumber(),
                productEntityPage.getSize(),
                responses
        );
    }

    private ProductFindResponse format(ProductEntity productEntity){
        ItemEntity itemEntity = itemService.findByUuid(productEntity.getItemUuid());
        return new ProductFindResponse(
                productEntity.getUuid(),
                productEntity.getItemUuid(),
                itemEntity.getNo(),
                productEntity.getSpecification(),
                productEntity.getUnit(),
                productEntity.getUnitPrice(),
                null
        );
    }

    private List<ProductFindAllResponse> format(List<ProductEntity> productEntities){
        List<ProductFindAllResponse> responses = new ArrayList<>();
        if(null == productEntities || productEntities.isEmpty()){
            return responses;
        }
        List<ItemEntity> itemEntities = itemService.findAll();
        ItemEntity itemEntity;
        List<ProductVendorEntity> productVendorEntities;
        List<UUID> vendorUuids;
        for(ProductEntity productEntity : productEntities){
            itemEntity = CommonUtil.getEntityByUuid(itemEntities, productEntity.getItemUuid());
            productVendorEntities = productVendorService.findAll(productEntity.getUuid());
            vendorUuids = getVendorUuids(productVendorEntities);
            responses.add(new ProductFindAllResponse(
                    productEntity.getUuid(),
                    productEntity.getItemUuid(),
                    itemEntity.getNo(),
                    productEntity.getSpecification(),
                    productEntity.getUnit(),
                    productEntity.getUnitPrice(),
                    null,
                    vendorUuids
            ));
        }
        return responses;
    }

    private List<UUID> getVendorUuids(List<ProductVendorEntity> productVendorEntities){
        List<UUID> vendorUuids = new ArrayList<>();
        if(null == productVendorEntities || productVendorEntities.isEmpty()){
            return vendorUuids;
        }
        for(ProductVendorEntity productVendorEntity : productVendorEntities){
            vendorUuids.add(productVendorEntity.getVendorUuid());
        }
        return vendorUuids;
    }

}
