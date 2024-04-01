package com.design.usecase.product;

import com.design.controller.common.response.CommonProductFindAllResponse;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductFindUseCaseImpl implements ProductFindUseCase {

    private final ProductService productService;

    private final ItemService itemService;

    private final ProductVendorService productVendorService;

    @Override
    public ProductFindResponse findByUuid(String productUuid) {
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
        Integer page = request.page();
        Integer size = null == request.size() ? 10 : request.size();
        Sort.Order orderItemUuid = new Sort.Order(Sort.Direction.ASC, "itemUuid");
        Sort.Order orderSpecification = new Sort.Order(Sort.Direction.ASC, "specification");
        Sort sort = Sort.by(orderItemUuid, orderSpecification);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductEntity> productEntityPage = productService.findAllLikeByPage(
                request.keyword(),
                pageable
        );
        List<ProductFindAllResponse> responses = format(productEntityPage.getContent());
        return new ProductFindPageResponse(
                productEntityPage.getTotalPages(),
                productEntityPage.getNumber(),
                productEntityPage.getSize(),
                responses
        );
    }

    @Override
    public List<CommonProductFindAllResponse> findAllCommon() {
        List<ProductEntity> productEntities = productService.findAll();
        return formatCommon(productEntities);
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
                productEntity.getCostPrice()
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
        List<String> vendorUuids;
        for(ProductEntity productEntity : productEntities){
            itemEntity = getItem(itemEntities, productEntity.getItemUuid());
            productVendorEntities = productVendorService.findAll(productEntity.getUuid());
            vendorUuids = getVendorUuids(productVendorEntities);
            responses.add(new ProductFindAllResponse(
                    productEntity.getUuid(),
                    productEntity.getItemUuid(),
                    itemEntity.getNo(),
                    productEntity.getSpecification(),
                    productEntity.getUnit(),
                    productEntity.getUnitPrice(),
                    productEntity.getCostPrice(),
                    vendorUuids
            ));
        }
        return responses;
    }

    private List<String> getVendorUuids(List<ProductVendorEntity> productVendorEntities){
        List<String> vendorUuids = new ArrayList<>();
        if(null == productVendorEntities || productVendorEntities.isEmpty()){
            return vendorUuids;
        }
        for(ProductVendorEntity productVendorEntity : productVendorEntities){
            vendorUuids.add(productVendorEntity.getVendorUuid());
        }
        return vendorUuids;
    }

    private ItemEntity getItem(List<ItemEntity> itemEntities, String itemUuid){
        if(null == itemEntities || itemEntities.isEmpty()){
            return null;
        }
        for(ItemEntity itemEntity : itemEntities){
            if(itemEntity.getUuid().equals(itemUuid)){
                return itemEntity;
            }
        }
        return null;
    }

    private List<CommonProductFindAllResponse> formatCommon(List<ProductEntity> productEntities){
        List<CommonProductFindAllResponse> responses = new ArrayList<>();
        if(null == productEntities || productEntities.isEmpty()){
            return responses;
        }
        List<ItemEntity> itemEntities = itemService.findAll();
        ItemEntity itemEntity;
        for(ProductEntity productEntity : productEntities){
            itemEntity = getItem(itemEntities, productEntity.getItemUuid());
            responses.add(new CommonProductFindAllResponse(
                    productEntity.getUuid(),
                    productEntity.getItemUuid(),
                    itemEntity.getNo(),
                    productEntity.getSpecification(),
                    productEntity.getUnit(),
                    productEntity.getUnitPrice(),
                    productEntity.getCostPrice()
            ));
        }
        return responses;
    }

}
