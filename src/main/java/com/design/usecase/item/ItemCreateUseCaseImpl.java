package com.design.usecase.item;

import com.design.controller.item.request.ItemCreateRequest;
import com.design.entity.item.ItemEntity;
import com.design.service.item.ItemService;
import com.design.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemCreateUseCaseImpl implements ItemCreateUseCase {

    private final ItemService itemService;

    @Override
    public void create(ItemCreateRequest request) {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setVendorUuid(request.vendorUuid());
        itemEntity.setName(request.name());
        itemService.create(itemEntity, JwtUtil.extractUsername());
    }

}
