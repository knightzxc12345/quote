package com.design.service.item;

import com.design.entity.item.ItemEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ItemService {

    ItemEntity create(ItemEntity itemEntity, UUID userUuid);

    void update(ItemEntity itemEntity, UUID userUuid);

    void delete(ItemEntity itemEntity, UUID userUuid);

    ItemEntity findByUuid(UUID itemUuid);

    List<ItemEntity> findAll();

    List<ItemEntity> findAllItemUuidIn(List<UUID> itemUuids);

    List<ItemEntity> findAllCommon();

    List<ItemEntity> findAllLike(
            String keyword
    );

    Page<ItemEntity> findAllLikeByPage(
            String keyword,
            Integer page,
            Integer size
    );

}
