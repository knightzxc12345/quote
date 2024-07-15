package com.design.usecase.item;

import com.design.controller.item.request.ItemCreateRequest;
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
public class ItemCreateUseCaseImpl implements ItemCreateUseCase {

    private final ItemService itemService;

    private final VendorProductService vendorProductService;

    private final ItemVendorProductService itemVendorProductService;

    @Override
    public void create(ItemCreateRequest request) {
        // 初始化品項
        ItemEntity itemEntity = init(request);
        // 初始化品項廠商產品清單
        List<ItemVendorProductEntity> itemVendorProductEntities = initItemVendorProducts(request, itemEntity);
        // 寫入品項
        itemService.create(itemEntity, JwtUtil.extractUserUuid());
        // 寫入品項廠商產品清單
        itemVendorProductService.createAll(itemVendorProductEntities, JwtUtil.extractUserUuid());
    }

    // 初始化品項
    private ItemEntity init(ItemCreateRequest request){
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setUuid(UUID.randomUUID());
        itemEntity.setNo(request.no());
        itemEntity.setName(request.name());
        itemEntity.setSpec(request.spec());
        itemEntity.setUnit(request.unit());
        itemEntity.setAmount(request.amount());
        return itemEntity;
    }

    // 初始化品項廠商產品清單
    private List<ItemVendorProductEntity> initItemVendorProducts(ItemCreateRequest request, ItemEntity itemEntity){
        List<ItemVendorProductEntity> itemVendorProductEntities = new ArrayList<>();
        List<ItemCreateRequest.VendorProduct> vendorProducts = request.vendorProducts();
        if(null == vendorProducts || vendorProducts.isEmpty()){
            return itemVendorProductEntities;
        }
        // 取得廠商產品uuid清單
        List<UUID> vendorProductUuids = getVendorProductUuids(vendorProducts);
        // 取得廠商產品清單
        List<VendorProductEntity> vendorProductEntities = vendorProductService.findAllIn(vendorProductUuids);
        VendorProductEntity vendorProductEntity;
        ItemVendorProductEntity itemVendorProductEntity;
        for(ItemCreateRequest.VendorProduct vendorProduct : vendorProducts){
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
    private List<UUID> getVendorProductUuids(List<ItemCreateRequest.VendorProduct> vendorProducts){
        List<UUID> vendorProductUuids = new ArrayList<>();
        if(null == vendorProducts || vendorProducts.isEmpty()){
            return vendorProductUuids;
        }
        for(ItemCreateRequest.VendorProduct vendorProduct : vendorProducts){
            vendorProductUuids.add(vendorProduct.vendorProductUuid());
        }
        return CommonUtil.removeDuplicates(vendorProductUuids);
    }

}
