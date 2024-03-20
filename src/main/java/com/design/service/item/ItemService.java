package com.design.service.item;

import com.design.entity.item.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {

    ItemEntity create(ItemEntity itemEntity, String userUuid);

    void update(ItemEntity itemEntity, String userUuid);

    void delete(ItemEntity itemEntity, String userUuid);

    ItemEntity findByUuid(String itemUuid);

    List<ItemEntity> findAll();

    List<ItemEntity> findAllLike(
            String vendorUuid,
            String keyword
    );

    Page<ItemEntity> findAllLikeByPage(
            String vendorUuid,
            String keyword,
            Pageable pageable
    );

}
