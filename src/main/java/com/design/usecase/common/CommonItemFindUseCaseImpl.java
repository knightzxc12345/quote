package com.design.usecase.common;

import com.design.controller.common.response.CommonItemFindAllResponse;
import com.design.entity.item.ItemEntity;
import com.design.entity.item_vendor_product.ItemVendorProductEntity;
import com.design.entity.vendor_product.VendorProductEntity;
import com.design.service.item.ItemService;
import com.design.service.item_vendor_producct.ItemVendorProductService;
import com.design.service.vendor_product.VendorProductService;
import com.design.utils.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommonItemFindUseCaseImpl implements CommonItemFindUseCase {

    private final ItemService itemService;

    private final ItemVendorProductService itemVendorProductService;

    private final VendorProductService vendorProductService;

    @Override
    public List<CommonItemFindAllResponse> findAll() {
        List<ItemEntity> itemEntities = itemService.findAllCommon();
        return format(itemEntities);
    }

    private List<CommonItemFindAllResponse> format(List<ItemEntity> itemEntities){
        List<CommonItemFindAllResponse> responses = new ArrayList<>();
        if(null == itemEntities || itemEntities.isEmpty()){
            return responses;
        }
        List<UUID> itemUuids = CommonUtil.getEntityUuids(itemEntities);
        // 取得所有產品項目
        List<ItemVendorProductEntity> itemVendorProductEntities = itemVendorProductService.findAllByItemUuidIn(itemUuids);
        // 取得廠商產品uuid清單
        List<UUID> vendorProductUuids = getVendorProductUuids(itemVendorProductEntities);
        // 取得廠商產品清單
        List<VendorProductEntity> vendorProductEntities = vendorProductService.findAllIn(vendorProductUuids);
        List<ItemVendorProductEntity> tempItemVendorProductEntities;
        BigDecimal unitPrice;
        for(ItemEntity itemEntity : itemEntities){
            // 取得回傳品項廠商產品清單
            tempItemVendorProductEntities = getItemVendorProducts(
                    itemVendorProductEntities,
                    itemEntity
            );
            // 取得回傳廠商產品
            unitPrice = getUnitPrice(
                    tempItemVendorProductEntities,
                    vendorProductEntities
            );
            responses.add(new CommonItemFindAllResponse(
                    itemEntity.getUuid(),
                    itemEntity.getNo(),
                    itemEntity.getName(),
                    itemEntity.getSpec(),
                    itemEntity.getUnit(),
                    unitPrice,
                    itemEntity.getAmount()
            ));
        }
        return responses;
    }

    // 取得廠商產品uuid清單
    private List<UUID> getVendorProductUuids(List<ItemVendorProductEntity> itemVendorProductEntities){
        List<UUID> vendorProductUuids = new ArrayList<>();
        if(null == itemVendorProductEntities || itemVendorProductEntities.isEmpty()){
            return vendorProductUuids;
        }
        for(ItemVendorProductEntity itemVendorProductEntity : itemVendorProductEntities){
            vendorProductUuids.add(itemVendorProductEntity.getVendorProductUuid());
        }
        return vendorProductUuids;
    }

    // 取得回傳品項廠商產品清單
    private List<ItemVendorProductEntity> getItemVendorProducts(List<ItemVendorProductEntity> itemVendorProductEntities, ItemEntity itemEntity){
        List<ItemVendorProductEntity> result = new ArrayList<>();
        if(null == itemVendorProductEntities || itemVendorProductEntities.isEmpty()){
            return result;
        }
        for(ItemVendorProductEntity itemVendorProductEntity : itemVendorProductEntities){
            if(!itemVendorProductEntity.getItemUuid().equals(itemEntity.getUuid())){
                continue;
            }
            result.add(itemVendorProductEntity);
        }
        return result;
    }

    private BigDecimal getUnitPrice(
            List<ItemVendorProductEntity> itemVendorProductEntities,
            List<VendorProductEntity> vendorProductEntities){
        BigDecimal total = new BigDecimal(0);
        BigDecimal unitPrice;
        if(null == itemVendorProductEntities || itemVendorProductEntities.isEmpty()){
            return total;
        }
        if(null == vendorProductEntities || vendorProductEntities.isEmpty()){
            return total;
        }
        VendorProductEntity vendorProductEntity;
        for(ItemVendorProductEntity itemVendorProductEntity : itemVendorProductEntities){
            vendorProductEntity = CommonUtil.getEntityByUuid(vendorProductEntities, itemVendorProductEntity.getVendorProductUuid());
            unitPrice = vendorProductEntity.getUnitPrice().multiply(new BigDecimal(itemVendorProductEntity.getQuantity()));
            total = total.add(unitPrice);
        }
        return total;
    }

}
