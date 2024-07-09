package com.design.repository.item_vendor_product;

import com.design.entity.item_vendor_product.ItemVendorProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemVendorProductRepository extends JpaRepository<ItemVendorProductEntity, Long> {

    ItemVendorProductEntity findByIsDeletedFalseAndUuid(UUID itemVendorProductUuid);

    List<ItemVendorProductEntity> findByIsDeletedFalseAndItemUuid(UUID itemUuid);

    List<ItemVendorProductEntity> findByIsDeletedFalseAndItemUuidInOrderByCreateTimeAsc(List<UUID> itemUuids);

}
