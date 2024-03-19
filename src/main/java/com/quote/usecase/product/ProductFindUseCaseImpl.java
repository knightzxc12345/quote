package com.quote.usecase.product;

import com.quote.controller.product.request.ProductFindRequest;
import com.quote.controller.product.response.ProductFindAllResponse;
import com.quote.controller.product.response.ProductFindPageResponse;
import com.quote.controller.product.response.ProductFindResponse;
import com.quote.entity.product.ProductEntity;
import com.quote.service.product.ProductService;
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
        List<ProductEntity> productEntities = productService.findAllLike(request.keyword());
        return format(productEntities);
    }

    @Override
    public ProductFindPageResponse findAllByPage(ProductFindRequest request) {
        Integer page = request.page();
        Integer size = null == request.size() ? 10 : request.size();
        Sort.Order orderName = new Sort.Order(Sort.Direction.DESC, "name");
        Sort.Order orderSpecification = new Sort.Order(Sort.Direction.DESC, "specification");
        Sort sort = Sort.by(orderName, orderSpecification);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductEntity> productEntityPage = productService.findAllLikeByPage(request.keyword(), pageable);
        List<ProductFindAllResponse> responses = format(productEntityPage.getContent());
        return new ProductFindPageResponse(
                productEntityPage.getTotalPages(),
                productEntityPage.getNumber(),
                productEntityPage.getSize(),
                responses
        );
    }

    private ProductFindResponse format(ProductEntity productEntity){
        if(null == productEntity){
            return null;
        }
        return new ProductFindResponse(
                productEntity.getUuid(),
                productEntity.getVendorUuid(),
                productEntity.getNo(),
                productEntity.getName(),
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
                    productEntity.getNo(),
                    productEntity.getName(),
                    productEntity.getSpecification(),
                    productEntity.getUnit(),
                    productEntity.getUnitPrice(),
                    productEntity.getCostPrice()
            ));
        }
        return responses;
    }

}
