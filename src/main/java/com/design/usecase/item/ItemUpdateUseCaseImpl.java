package com.design.usecase.item;

import com.design.controller.item.request.ItemUpdateRequest;
import com.design.entity.item.ItemEntity;
import com.design.service.item.ItemService;
import com.design.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemUpdateUseCaseImpl implements ItemUpdateUseCase {

    private final ItemService itemService;

    @Override
    public void update(ItemUpdateRequest request, UUID itemUuid) {
        ItemEntity itemEntity = itemService.findByUuid(itemUuid);
        itemEntity = update(itemEntity, request);
        itemService.update(itemEntity, JwtUtil.extractUserUuid());
    }

    private ItemEntity update(ItemEntity itemEntity, ItemUpdateRequest request){
        itemEntity.setNo(request.no());
        itemEntity.setName(request.name());
        return itemEntity;
    }

}
