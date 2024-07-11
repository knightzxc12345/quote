package com.design.usecase.item;

import com.design.controller.item.request.ItemFindRequest;
import com.design.controller.item.response.ItemFindAllResponse;
import com.design.controller.item.response.ItemFindPageResponse;
import com.design.controller.item.response.ItemFindResponse;
import com.design.entity.item.ItemEntity;
import com.design.entity.item_vendor_product.ItemVendorProductEntity;
import com.design.entity.vendor.VendorEntity;
import com.design.entity.vendor_product.VendorProductEntity;
import com.design.service.item.ItemService;
import com.design.service.item_vendor_producct.ItemVendorProductService;
import com.design.service.vendor.VendorService;
import com.design.service.vendor_product.VendorProductService;
import com.design.utils.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemFindUseCaseImpl implements ItemFindUseCase {

    private final ItemService itemService;

    private final ItemVendorProductService itemVendorProductService;

    private final VendorProductService vendorProductService;

    private final VendorService vendorService;

    @Override
    public ItemFindResponse findByUuid(UUID itemUuid) {
        // 取得品項
        ItemEntity itemEntity = itemService.findByUuid(itemUuid);
        // 取得品項廠商產品清單
        List<ItemVendorProductEntity> itemVendorProductEntities = itemVendorProductService.findAllByItemUuid(itemEntity.getUuid());
        return format(itemEntity, itemVendorProductEntities);
    }

    @Override
    public List<ItemFindAllResponse> findAll(ItemFindRequest request) {
        List<ItemEntity> itemEntities = itemService.findAllLike(
                request.keyword()
        );
        return format(itemEntities);
    }

    @Override
    public ItemFindPageResponse findAllByPage(ItemFindRequest request) {
        Page<ItemEntity> itemEntityPage = itemService.findAllLikeByPage(
                request.keyword(),
                request.page(),
                request.size()
        );
        List<ItemFindAllResponse> responses = format(itemEntityPage.getContent());
        return new ItemFindPageResponse(
                itemEntityPage.getTotalPages(),
                itemEntityPage.getNumber(),
                itemEntityPage.getSize(),
                responses
        );
    }

    private ItemFindResponse format(
            ItemEntity itemEntity,
            List<ItemVendorProductEntity> itemVendorProductEntities){
        List<ItemFindResponse.vendorProduct> vendorProducts = getItemVendorProducts(itemVendorProductEntities);
        return new ItemFindResponse(
                itemEntity.getUuid(),
                itemEntity.getNo(),
                itemEntity.getName(),
                vendorProducts
        );
    }

    // 取得品項廠商產品
    private List<ItemFindResponse.vendorProduct> getItemVendorProducts(List<ItemVendorProductEntity> itemVendorProductEntities){
        List<ItemFindResponse.vendorProduct> vendorProducts = new ArrayList<>();
        if(null == itemVendorProductEntities || itemVendorProductEntities.isEmpty()){
            return vendorProducts;
        }
        for(ItemVendorProductEntity itemVendorProductEntity : itemVendorProductEntities){
            vendorProducts.add(new ItemFindResponse.vendorProduct(
                    itemVendorProductEntity.getVendorUuid(),
                    itemVendorProductEntity.getVendorProductUuid()
            ));
        }
        return vendorProducts;
    }

    private List<ItemFindAllResponse> format(List<ItemEntity> itemEntities){
        List<ItemFindAllResponse> responses = new ArrayList<>();
        if(null == itemEntities || itemEntities.isEmpty()){
            return responses;
        }
        // 取得品項uuid清單
        List<UUID> itemUuids = CommonUtil.getEntityUuids(itemEntities);
        // 取得品項廠商產品清單
        List<ItemVendorProductEntity> itemVendorProductEntities = itemVendorProductService.findAllByItemUuidIn(itemUuids);
        // 取得廠商產品uuid清單
        List<UUID> vendorProductUuids = getVendorProductUuids(itemVendorProductEntities);
        // 取得廠商產品清單
        List<VendorProductEntity> vendorProductEntities = vendorProductService.findAllIn(vendorProductUuids);
        // 取得廠商清單
        List<VendorEntity> vendorEntities = vendorService.findAll();
        List<ItemVendorProductEntity> tempItemVendorProductEntities;
        List<ItemFindAllResponse.VendorProduct> vendorProducts;
        for(ItemEntity itemEntity : itemEntities){
            // 取得回傳品項廠商產品清單
            tempItemVendorProductEntities = getItemVendorProducts(
                    itemVendorProductEntities,
                    itemEntity
            );
            // 取得回傳廠商產品
            vendorProducts = getVendorProducts(
                    tempItemVendorProductEntities,
                    vendorProductEntities,
                    vendorEntities
            );
            responses.add(new ItemFindAllResponse(
                    itemEntity.getUuid(),
                    itemEntity.getNo(),
                    itemEntity.getName(),
                    itemEntity.getSpec(),
                    vendorProducts
            ));
        }
        return responses;
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

    // 取得回傳廠商產品
    private List<ItemFindAllResponse.VendorProduct> getVendorProducts(
            List<ItemVendorProductEntity> itemVendorProductEntities,
            List<VendorProductEntity> vendorProductEntities,
            List<VendorEntity> vendorEntities){
        List<ItemFindAllResponse.VendorProduct> vendorProducts = new ArrayList<>();
        if(null == itemVendorProductEntities || itemVendorProductEntities.isEmpty()){
            return vendorProducts;
        }
        VendorProductEntity vendorProductEntity;
        VendorEntity vendorEntity;
        for(ItemVendorProductEntity itemVendorProductEntity : itemVendorProductEntities){
            vendorProductEntity = CommonUtil.getEntityByUuid(vendorProductEntities, itemVendorProductEntity.getVendorProductUuid());
            if(null == vendorProductEntity){
                continue;
            }
            vendorEntity = CommonUtil.getEntityByUuid(vendorEntities, vendorProductEntity.getVendorUuid());
            if(null == vendorEntity){
                continue;
            }
            vendorProducts.add(new ItemFindAllResponse.VendorProduct(
                    vendorEntity.getUuid(),
                    vendorEntity.getName(),
                    vendorProductEntity.getUuid(),
                    vendorProductEntity.getName(),
                    itemVendorProductEntity.getQty(),
                    vendorProductEntity.getUnitPrice()
            ));
        }
        return vendorProducts;
    }

}
