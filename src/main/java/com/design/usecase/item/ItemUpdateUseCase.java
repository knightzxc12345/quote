package com.design.usecase.item;

import com.design.controller.item.request.ItemUpdateRequest;

public interface ItemUpdateUseCase {

    void update(ItemUpdateRequest request, String itemUuid);

}
