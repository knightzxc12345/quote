package com.design.usecase.item;

import com.design.entity.item.ItemEntity;
import com.design.entity.item_vendor_product.ItemVendorProductEntity;
import com.design.service.item.ItemService;
import com.design.service.item_vendor_producct.ItemVendorProductService;
import com.design.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemDeleteUseCaseImpl implements ItemDeleteUseCase {

    private final ItemService itemService;

    private final ItemVendorProductService itemVendorProductService;

    @Override
    public void delete(UUID itemUuid) {
        // 取得品項
        ItemEntity itemEntity = itemService.findByUuid(itemUuid);
        // 取得品項廠商產品清單
        List<ItemVendorProductEntity> itemVendorProductEntities = itemVendorProductService.findAllByItemUuid(itemEntity.getUuid());
        // 刪除品項
        itemService.delete(itemEntity, JwtUtil.extractUserUuid());
        // 刪除品項廠商產品清單
        itemVendorProductService.deleteAll(itemVendorProductEntities, JwtUtil.extractUserUuid());
    }

}
