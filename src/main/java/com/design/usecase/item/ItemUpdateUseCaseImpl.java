package com.design.usecase.item;

import com.design.controller.item.request.ItemUpdateRequest;
import com.design.entity.item.ItemEntity;
import com.design.service.item.ItemService;
import com.design.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemUpdateUseCaseImpl implements ItemUpdateUseCase {

    private final ItemService itemService;

    @Override
    public void update(ItemUpdateRequest request, String itemUuid) {
        ItemEntity itemEntity = itemService.findByUuid(itemUuid);
        itemEntity.setVendorUuid(request.vendorUuid());
        itemEntity.setName(request.name());
        itemService.update(itemEntity, JwtUtil.extractUsername());
    }

}
