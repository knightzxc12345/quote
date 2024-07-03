package com.design.usecase.common;

import com.design.controller.common.response.CommonItemFindAllResponse;
import com.design.entity.item.ItemEntity;
import com.design.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonItemFindUseCaseImpl implements CommonItemFindUseCase {

    private final ItemService itemService;

    @Override
    public List<CommonItemFindAllResponse> findAll() {
        List<ItemEntity> itemEntities = itemService.findAllCommon();
        return formatCommon(itemEntities);
    }

    private List<CommonItemFindAllResponse> formatCommon(List<ItemEntity> itemEntities){
        List<CommonItemFindAllResponse> responses = new ArrayList<>();
        if(null == itemEntities || itemEntities.isEmpty()){
            return responses;
        }
        for(ItemEntity itemEntity : itemEntities){
            responses.add(new CommonItemFindAllResponse(
                    itemEntity.getUuid(),
                    itemEntity.getNo(),
                    itemEntity.getName()
            ));
        }
        return responses;
    }

}
