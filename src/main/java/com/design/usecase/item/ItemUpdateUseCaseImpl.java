package com.design.usecase.item;

import com.design.controller.item.request.ItemUpdateRequest;
import com.design.entity.item.ItemEntity;
import com.design.entity.item_vendor_product.ItemVendorProductEntity;
import com.design.entity.vendor_product.VendorProductEntity;
import com.design.service.item.ItemService;
import com.design.service.item_vendor_producct.ItemVendorProductService;
import com.design.service.vendor_product.VendorProductService;
import com.design.utils.CommonUtil;
import com.design.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemUpdateUseCaseImpl implements ItemUpdateUseCase {

    private final ItemService itemService;

    private final ItemVendorProductService itemVendorProductService;

    private final VendorProductService vendorProductService;

    @Override
    public void update(ItemUpdateRequest request, UUID itemUuid) {
        // 取得品項
        ItemEntity itemEntity = itemService.findByUuid(itemUuid);
        itemEntity = update(itemEntity, request);
        // 取得舊的品項廠商產品清單
        List<ItemVendorProductEntity> oldItemVendorProductEntities = itemVendorProductService.findAllByItemUuid(itemEntity.getUuid());
        // 取得新的品項廠商產品清單
        List<ItemVendorProductEntity> newItemVendorProductEntities = initVendorProducts(request, itemEntity);
        // 更新品項
        itemService.update(itemEntity, JwtUtil.extractUserUuid());
        // 刪除舊的品項廠商產品清單
        itemVendorProductService.deleteAll(oldItemVendorProductEntities, JwtUtil.extractUserUuid());
        // 新增新的品項廠商產品清單
        itemVendorProductService.createAll(newItemVendorProductEntities, JwtUtil.extractUserUuid());
    }

    // 初始化品項廠商產品清單
    private List<ItemVendorProductEntity> initVendorProducts(ItemUpdateRequest request, ItemEntity itemEntity){
        List<ItemVendorProductEntity> itemVendorProductEntities = new ArrayList<>();
        List<ItemUpdateRequest.VendorProduct> vendorProducts = request.vendorProducts();
        if(null == vendorProducts || vendorProducts.isEmpty()){
            return itemVendorProductEntities;
        }
        // 取得廠商產品uuid清單
        List<UUID> vendorProductUuids = getVendorProductUuids(vendorProducts);
        // 取得廠商產品清單
        List<VendorProductEntity> vendorProductEntities = vendorProductService.findAllIn(vendorProductUuids);
        ItemVendorProductEntity itemVendorProductEntity;
        VendorProductEntity vendorProductEntity;
        for(ItemUpdateRequest.VendorProduct vendorProduct : vendorProducts){
            vendorProductEntity = CommonUtil.getEntityByUuid(vendorProductEntities, vendorProduct.vendorProductUuid());
            if(null == vendorProductEntity){
                continue;
            }
            itemVendorProductEntity = new ItemVendorProductEntity();
            itemVendorProductEntity.setVendorUuid(vendorProductEntity.getVendorUuid());
            itemVendorProductEntity.setVendorProductUuid(vendorProduct.vendorProductUuid());
            itemVendorProductEntity.setItemUuid(itemEntity.getUuid());
            itemVendorProductEntity.setQty(vendorProduct.qty());
            itemVendorProductEntities.add(itemVendorProductEntity);
        }
        return itemVendorProductEntities;
    }

    // 取得廠商產品uuid清單
    private List<UUID> getVendorProductUuids(List<ItemUpdateRequest.VendorProduct> vendorProducts){
        List<UUID> vendorProductUuids = new ArrayList<>();
        if(null == vendorProducts || vendorProducts.isEmpty()){
            return vendorProductUuids;
        }
        for(ItemUpdateRequest.VendorProduct vendorProduct : vendorProducts){
            vendorProductUuids.add(vendorProduct.vendorProductUuid());
        }
        return CommonUtil.removeDuplicates(vendorProductUuids);
    }

    // 更新品項
    private ItemEntity update(ItemEntity itemEntity, ItemUpdateRequest request){
        itemEntity.setNo(request.no());
        itemEntity.setName(request.name());
        itemEntity.setSpec(request.spec());
        return itemEntity;
    }

}
