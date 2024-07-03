package com.design.usecase.item;

import com.design.controller.item.request.ItemFindRequest;
import com.design.controller.item.response.ItemFindAllResponse;
import com.design.controller.item.response.ItemFindPageResponse;
import com.design.controller.item.response.ItemFindResponse;

import java.util.List;
import java.util.UUID;

public interface ItemFindUseCase {

    ItemFindResponse findByUuid(UUID itemUuid);

    List<ItemFindAllResponse> findAll(ItemFindRequest request);

    ItemFindPageResponse findAllByPage(ItemFindRequest request);

}
