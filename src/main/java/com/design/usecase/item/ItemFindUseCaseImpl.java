package com.design.usecase.item;

import com.design.controller.item.request.ItemFindRequest;
import com.design.controller.item.response.ItemFindAllResponse;
import com.design.controller.item.response.ItemFindPageResponse;
import com.design.controller.item.response.ItemFindResponse;
import com.design.entity.item.ItemEntity;
import com.design.service.item.ItemService;
import com.design.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemFindUseCaseImpl implements ItemFindUseCase {

    private final ItemService itemService;

    private final ProductService productService;

    @Override
    public ItemFindResponse findByUuid(UUID itemUuid) {
        ItemEntity itemEntity = itemService.findByUuid(itemUuid);
        return format(itemEntity);
    }

    @Override
    public List<ItemFindAllResponse> findAll(ItemFindRequest request) {
        List<ItemEntity> itemEntities = itemService.findAllLike(
                request.keyword()
        );
        return format(itemEntities);
    }

    @Override
    public ItemFindPageResponse findAllByPage(ItemFindRequest request) {
        Page<ItemEntity> itemEntityPage = itemService.findAllLikeByPage(
                request.keyword(),
                request.page(),
                request.size()
        );
        List<ItemFindAllResponse> responses = format(itemEntityPage.getContent());
        return new ItemFindPageResponse(
                itemEntityPage.getTotalPages(),
                itemEntityPage.getNumber(),
                itemEntityPage.getSize(),
                responses
        );
    }

    private ItemFindResponse format(ItemEntity itemEntity){
        return new ItemFindResponse(
                itemEntity.getUuid(),
                itemEntity.getNo(),
                itemEntity.getName()
        );
    }

    private List<ItemFindAllResponse> format(List<ItemEntity> itemEntities){
        List<ItemFindAllResponse> responses = new ArrayList<>();
        if(null == itemEntities || itemEntities.isEmpty()){
            return responses;
        }
        for(ItemEntity itemEntity : itemEntities){
            responses.add(new ItemFindAllResponse(
                    itemEntity.getUuid(),
                    itemEntity.getNo(),
                    itemEntity.getName()
            ));
        }
        return responses;
    }

}
