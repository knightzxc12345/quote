package com.design.service.item_vendor_producct;

import com.design.entity.item_vendor_product.ItemVendorProductEntity;

import java.util.List;
import java.util.UUID;

public interface ItemVendorProductService {

    void createAll(List<ItemVendorProductEntity> itemVendorProductEntities, UUID userUuid);

    void deleteAll(List<ItemVendorProductEntity> itemVendorProductEntities, UUID userUuid);

    ItemVendorProductEntity findByUuid(UUID itemVendorProductUuid);

    List<ItemVendorProductEntity> findAllByItemUuid(UUID itemUuid);

}
