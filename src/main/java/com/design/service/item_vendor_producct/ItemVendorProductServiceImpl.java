package com.design.service.item_vendor_producct;

import com.design.entity.item_vendor_product.ItemVendorProductEntity;
import com.design.repository.item_vendor_product.ItemVendorProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemVendorProductServiceImpl implements ItemVendorProductService {

    private final ItemVendorProductRepository itemVendorProductRepository;

    @Override
    public void createAll(List<ItemVendorProductEntity> itemVendorProductEntities, UUID userUuid) {
        if(null == itemVendorProductEntities || itemVendorProductEntities.isEmpty()){
            return;
        }
        for(ItemVendorProductEntity itemVendorProductEntity : itemVendorProductEntities){
            itemVendorProductEntity.setIsDeleted(false);
            itemVendorProductEntity.setUuid(UUID.randomUUID());
            itemVendorProductEntity.setCreateTime(Instant.now());
            itemVendorProductEntity.setCreateUser(userUuid);
        }
        itemVendorProductRepository.saveAll(itemVendorProductEntities);
    }

    @Override
    public void deleteAll(List<ItemVendorProductEntity> itemVendorProductEntities, UUID userUuid) {
        if(null == itemVendorProductEntities || itemVendorProductEntities.isEmpty()){
            return;
        }
        for(ItemVendorProductEntity itemVendorProductEntity : itemVendorProductEntities){
            itemVendorProductEntity.setIsDeleted(true);
            itemVendorProductEntity.setDeletedTime(Instant.now());
            itemVendorProductEntity.setDeletedUser(userUuid);
        }
        itemVendorProductRepository.saveAll(itemVendorProductEntities);
    }

    @Override
    public ItemVendorProductEntity findByUuid(UUID itemVendorProductUuid) {
        return itemVendorProductRepository.findByIsDeletedFalseAndUuid(itemVendorProductUuid);
    }

    @Override
    public List<ItemVendorProductEntity> findAllByItemUuid(UUID itemUuid) {
        return itemVendorProductRepository.findByIsDeletedFalseAndItemUuid(itemUuid);
    }

}
