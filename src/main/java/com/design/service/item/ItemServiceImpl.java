package com.design.service.item;

import com.design.base.eunms.ItemEnum;
import com.design.entity.item.ItemEntity;
import com.design.handler.BusinessException;
import com.design.repository.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public ItemEntity create(ItemEntity itemEntity, String userUuid) {
        ItemEntity isExists = itemRepository.findByIsDeletedFalseAndVendorUuidAndName(
                itemEntity.getVendorUuid(),
                itemEntity.getName()
        );
        if(null != isExists){
            throw new BusinessException(ItemEnum.IT0001);
        }
        itemEntity.setUuid(UUID.randomUUID().toString());
        itemEntity.setIsDeleted(false);
        itemEntity.setCreateTime(Instant.now());
        itemEntity.setCreateUser(userUuid);
        return itemRepository.save(itemEntity);
    }

    @Override
    public void update(ItemEntity itemEntity, String userUuid) {
        ItemEntity isExists = itemRepository.findByIsDeletedFalseAndVendorUuidAndName(
                itemEntity.getVendorUuid(),
                itemEntity.getName()
        );
        if(null != isExists && !itemEntity.getUuid().equals(isExists.getUuid())){
            throw new BusinessException(ItemEnum.IT0001);
        }
        itemEntity.setModifiedTime(Instant.now());
        itemEntity.setModifiedUser(userUuid);
        itemRepository.save(itemEntity);
    }

    @Override
    public void delete(ItemEntity itemEntity, String userUuid) {
        itemEntity.setIsDeleted(true);
        itemEntity.setDeletedTime(Instant.now());
        itemEntity.setDeletedUser(userUuid);
        itemRepository.save(itemEntity);
    }

    @Override
    public ItemEntity findByUuid(String itemUuid) {
        return itemRepository.findByIsDeletedFalseAndUuid(itemUuid);
    }

    @Override
    public List<ItemEntity> findAll() {
        return itemRepository.findAll();
    }

    @Override
    public List<ItemEntity> findAllLike(
            String vendorUuid,
            String keyword) {
        return itemRepository.findAll(
                vendorUuid,
                keyword
        );
    }

    @Override
    public Page<ItemEntity> findAllLikeByPage(
            String vendorUuid,
            String keyword,
            Pageable pageable) {
        return itemRepository.findAllByPage(
                vendorUuid,
                keyword,
                pageable
        );
    }

}
