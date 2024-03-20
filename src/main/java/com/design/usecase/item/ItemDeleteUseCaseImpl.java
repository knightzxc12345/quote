package com.design.usecase.item;

import com.design.entity.item.ItemEntity;
import com.design.service.item.ItemService;
import com.design.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemDeleteUseCaseImpl implements ItemDeleteUseCase {

    private final ItemService itemService;

    @Override
    public void delete(String itemUuid) {
        ItemEntity itemEntity = itemService.findByUuid(itemUuid);
        itemService.delete(itemEntity, JwtUtil.extractUsername());
    }

}
