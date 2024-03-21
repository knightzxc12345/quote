package com.design.usecase.product;

import com.design.controller.common.response.CommonProductFindAllResponse;
import com.design.controller.product.request.ProductFindRequest;
import com.design.controller.product.response.ProductFindAllResponse;
import com.design.controller.product.response.ProductFindPageResponse;
import com.design.controller.product.response.ProductFindResponse;
import com.design.entity.product.ProductEntity;
import com.design.service.product.ProductService;
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

    @Override
    public ProductFindResponse findByUuid(String productUuid) {
        ProductEntity productEntity = productService.findByUuid(productUuid);
        return format(productEntity);
    }

    @Override
    public List<ProductFindAllResponse> findAll(ProductFindRequest request) {
        List<ProductEntity> productEntities = productService.findAllLike(
                request.vendorUuid(),
                request.keyword()
        );
        return format(productEntities);
    }

    @Override
    public ProductFindPageResponse findAllByPage(ProductFindRequest request) {
        Integer page = request.page();
        Integer size = null == request.size() ? 10 : request.size();
        Sort.Order orderVendorUuid = new Sort.Order(Sort.Direction.ASC, "vendorUuid");
        Sort.Order orderItemUuid = new Sort.Order(Sort.Direction.ASC, "itemUuid");
        Sort.Order orderNo = new Sort.Order(Sort.Direction.ASC, "no");
        Sort.Order orderSpecification = new Sort.Order(Sort.Direction.ASC, "specification");
        Sort sort = Sort.by(orderVendorUuid, orderItemUuid, orderNo, orderSpecification);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductEntity> productEntityPage = productService.findAllLikeByPage(
                request.vendorUuid(),
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
        return new ProductFindResponse(
                productEntity.getUuid(),
                productEntity.getVendorUuid(),
                productEntity.getItemUuid(),
                productEntity.getNo(),
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
        for(ProductEntity productEntity : productEntities){
            responses.add(new ProductFindAllResponse(
                    productEntity.getUuid(),
                    productEntity.getVendorUuid(),
                    productEntity.getItemUuid(),
                    productEntity.getNo(),
                    productEntity.getSpecification(),
                    productEntity.getUnit(),
                    productEntity.getUnitPrice(),
                    productEntity.getCostPrice()
            ));
        }
        return responses;
    }

    private List<CommonProductFindAllResponse> formatCommon(List<ProductEntity> productEntities){
        List<CommonProductFindAllResponse> responses = new ArrayList<>();
        if(null == productEntities || productEntities.isEmpty()){
            return responses;
        }
        for(ProductEntity productEntity : productEntities){
            responses.add(new CommonProductFindAllResponse(
                    productEntity.getUuid(),
                    productEntity.getVendorUuid(),
                    productEntity.getItemUuid(),
                    productEntity.getNo(),
                    productEntity.getItemUuid(),
                    productEntity.getSpecification(),
                    productEntity.getUnit(),
                    productEntity.getUnitPrice(),
                    productEntity.getCostPrice()
            ));
        }
        return responses;
    }

}
