package com.design.usecase.item;

import com.design.controller.common.response.CommonItemFindAllResponse;
import com.design.controller.item.request.ItemFindRequest;
import com.design.controller.item.response.ItemFindAllResponse;
import com.design.controller.item.response.ItemFindPageResponse;
import com.design.controller.item.response.ItemFindResponse;
import com.design.entity.item.ItemEntity;
import com.design.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemFindUseCaseImpl implements ItemFindUseCase {

    private final ItemService itemService;

    @Override
    public ItemFindResponse findByUuid(String itemUuid) {
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
        Integer page = request.page();
        Integer size = null == request.size() ? 10 : request.size();
        Sort.Order orderNo = new Sort.Order(Sort.Direction.ASC, "no");
        Sort.Order orderName = new Sort.Order(Sort.Direction.ASC, "name");
        Sort sort = Sort.by(orderNo, orderName);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ItemEntity> itemEntityPage = itemService.findAllLikeByPage(
                request.keyword(),
                pageable
        );
        List<ItemFindAllResponse> responses = format(itemEntityPage.getContent());
        return new ItemFindPageResponse(
                itemEntityPage.getTotalPages(),
                itemEntityPage.getNumber(),
                itemEntityPage.getSize(),
                responses
        );
    }

    @Override
    public List<CommonItemFindAllResponse> findAllCommon() {
        List<ItemEntity> itemEntities = itemService.findAll();
        return formatCommon(itemEntities);
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

    private List<CommonItemFindAllResponse> formatCommon(List<ItemEntity> itemEntities){
        List<CommonItemFindAllResponse> responses = new ArrayList<>();
        if(null == itemEntities || itemEntities.isEmpty()){
            return responses;
        }
        for(ItemEntity itemEntity : itemEntities){
            responses.add(new CommonItemFindAllResponse(
                    itemEntity.getNo(),
                    itemEntity.getName()
            ));
        }
        return responses;
    }

}
