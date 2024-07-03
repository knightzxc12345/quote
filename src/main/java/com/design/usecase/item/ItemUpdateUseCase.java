package com.design.usecase.item;

import com.design.controller.item.request.ItemUpdateRequest;

import java.util.UUID;

public interface ItemUpdateUseCase {

    void update(ItemUpdateRequest request, UUID itemUuid);

}
