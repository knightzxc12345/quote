package com.design.usecase.common;

import com.design.controller.common.response.CommonProductFindAllResponse;
import com.design.entity.item.ItemEntity;
import com.design.entity.product.ProductEntity;
import com.design.service.item.ItemService;
import com.design.service.product.ProductService;
import com.design.utils.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonProductFindUseCaseImpl implements CommonProductFindUseCase {

    private final ProductService productService;

    private final ItemService itemService;

    @Override
    public List<CommonProductFindAllResponse> findAll() {
        List<ProductEntity> productEntities = productService.findAll();
        return formatCommon(productEntities);
    }

    private List<CommonProductFindAllResponse> formatCommon(List<ProductEntity> productEntities){
        List<CommonProductFindAllResponse> responses = new ArrayList<>();
        if(null == productEntities || productEntities.isEmpty()){
            return responses;
        }
        List<ItemEntity> itemEntities = itemService.findAll();
        ItemEntity itemEntity;
        for(ProductEntity productEntity : productEntities){
            itemEntity = CommonUtil.getEntityByUuid(itemEntities, productEntity.getItemUuid());
            responses.add(new CommonProductFindAllResponse(
                    productEntity.getUuid(),
                    productEntity.getItemUuid(),
                    itemEntity.getNo(),
                    productEntity.getSpecification(),
                    productEntity.getUnit(),
                    null
            ));
        }
        return responses;
    }

}
