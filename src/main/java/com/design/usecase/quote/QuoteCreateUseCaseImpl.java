package com.design.usecase.quote;

import com.design.controller.quote.request.QuoteCreateRequest;
import com.design.entity.customer.CustomerEntity;
import com.design.entity.item.ItemEntity;
import com.design.entity.item_vendor_product.ItemVendorProductEntity;
import com.design.entity.quote.QuoteEntity;
import com.design.entity.quote_detail.QuoteDetailEntity;
import com.design.entity.user.UserEntity;
import com.design.entity.vendor_product.VendorProductEntity;
import com.design.entity.vendor_quote.VendorQuoteEntity;
import com.design.entity.vendor_quote_detail.VendorQuoteDetailEntity;
import com.design.service.customer.CustomerService;
import com.design.service.item.ItemService;
import com.design.service.item_vendor_producct.ItemVendorProductService;
import com.design.service.quote.QuoteService;
import com.design.service.quote_detail.QuoteDetailService;
import com.design.service.user.UserService;
import com.design.service.vendor_product.VendorProductService;
import com.design.service.vendor_quote.VendorQuoteService;
import com.design.service.vendor_quote_detail.VendorQuoteDetailService;
import com.design.utils.CommonUtil;
import com.design.utils.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class QuoteCreateUseCaseImpl implements QuoteCreateUseCase {

    private final UserService userService;

    private final CustomerService customerService;

    private final ItemService itemService;

    private final ItemVendorProductService itemVendorProductService;

    private final VendorProductService vendorProductService;

    private final QuoteService quoteService;

    private final QuoteDetailService quoteDetailService;

    private final VendorQuoteService vendorQuoteService;

    private final VendorQuoteDetailService vendorQuoteDetailService;

    @Override
    public void create(QuoteCreateRequest request) {
        // 取得使用者
        UserEntity userEntity = userService.findByUuid(request.userUuid());
        // 取得客戶
        CustomerEntity customerEntity = customerService.findByUuid(request.customerUuid());
        // 取得品項uuid清單
        List<UUID> itemUuids = getItemUuids(request);
        // 取得項目清單
        List<ItemEntity> itemEntities = itemService.findAllItemUuidIn(itemUuids);
        // 取得品項廠商產品清單
        List<ItemVendorProductEntity> itemVendorProductEntities = itemVendorProductService.findAllByItemUuidIn(itemUuids);
        // 取得廠商產品uuid清單
        List<UUID> vendorProductUuids = getVendorProductUuids(itemVendorProductEntities);
        // 取得廠商產品清單
        List<VendorProductEntity> vendorProductEntities = vendorProductService.findAllIn(vendorProductUuids);
        // 初始化報價單
        QuoteEntity quoteEntity = initQuote(
                userEntity,
                customerEntity,
                request
        );
        // 取得報價單明細清單
        List<QuoteDetailEntity> quoteDetailEntities = initQuoteDetails(
                itemEntities,
                itemVendorProductEntities,
                vendorProductEntities,
                quoteEntity,
                request
        );
        // 更新報價單金額
        quoteEntity = updateQuoteAmount(quoteEntity, quoteDetailEntities);
        // 初始化廠商出貨清單
        List<VendorQuoteEntity> vendorQuoteEntities = initVendorQuotes(
                itemVendorProductEntities,
                quoteEntity
        );
        // 初始化廠商報價單明細清單
        List<VendorQuoteDetailEntity> vendorQuoteDetailEntities = initVendorQuoteDetails(
                itemVendorProductEntities,
                vendorProductEntities,
                vendorQuoteEntities,
                request
        );
        // 更新報價單清單
        vendorQuoteEntities = updateVendorQuotes(vendorQuoteEntities, vendorQuoteDetailEntities);
        // 新增報價單
        quoteService.create(quoteEntity, JwtUtil.extractUserUuid());
        // 新增報價單明細
        quoteDetailService.createAll(quoteDetailEntities, JwtUtil.extractUserUuid());
        // 新增廠商出貨清單
        vendorQuoteService.createAll(vendorQuoteEntities, JwtUtil.extractUserUuid());
        // 新增廠商出貨明細清單
        vendorQuoteDetailService.createAll(vendorQuoteDetailEntities, JwtUtil.extractUserUuid());
    }

    // 初始化報價單
    private QuoteEntity initQuote(UserEntity userEntity, CustomerEntity customerEntity, QuoteCreateRequest request){
        QuoteEntity quoteEntity = new QuoteEntity();
        quoteEntity.setUuid(UUID.randomUUID());
        quoteEntity.setUserUuid(userEntity.getUuid());
        quoteEntity.setUserName(userEntity.getName());
        quoteEntity.setCustomerUuid(customerEntity.getUuid());
        quoteEntity.setCustomerName(customerEntity.getName());
        quoteEntity.setCustomerAddress(customerEntity.getAddress());
        quoteEntity.setCustomerVatNumber(customerEntity.getVatNumber());
        quoteEntity.setUnderTakerName(request.underTakerName());
        quoteEntity.setUnderTakerTel(request.underTakerTel());
        return quoteEntity;
    }

    // 取得品項uuid清單
    private List<UUID> getItemUuids(QuoteCreateRequest request){
        List<UUID> itemUuids = new ArrayList<>();
        List<QuoteCreateRequest.Item> items = request.items();
        if(null == items || items.isEmpty()){
            return itemUuids;
        }
        for(QuoteCreateRequest.Item item : items){
            itemUuids.add(item.itemUuid());
        }
        return itemUuids;
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

    // 取得報價單明細清單
    private List<QuoteDetailEntity> initQuoteDetails(
            List<ItemEntity> itemEntities,
            List<ItemVendorProductEntity> itemVendorProductEntities,
            List<VendorProductEntity> vendorProductEntities,
            QuoteEntity quoteEntity,
            QuoteCreateRequest request){
        List<QuoteCreateRequest.Item> items = request.items();
        List<QuoteDetailEntity> quoteDetailEntities = new ArrayList<>();
        if(null == itemVendorProductEntities || itemVendorProductEntities.isEmpty()){
            return quoteDetailEntities;
        }
        if(null == vendorProductEntities || vendorProductEntities.isEmpty()){
            return quoteDetailEntities;
        }
        ItemEntity itemEntity;
        QuoteDetailEntity quoteDetailEntity;
        List<ItemVendorProductEntity> tempItemVendorProductEntities;
        List<VendorProductEntity> tempVendorProductEntities;
        BigDecimal costPrice;
        for(QuoteCreateRequest.Item item : items){
            itemEntity = CommonUtil.getEntityByUuid(itemEntities, item.itemUuid());
            if(null == itemEntity){
                continue;
            }
            // 取得品項廠商產品清單
            tempItemVendorProductEntities = getItemVendorProducts(itemVendorProductEntities, itemEntity);
            if(null == tempItemVendorProductEntities || tempItemVendorProductEntities.isEmpty()){
                continue;
            }
            // 取得廠商產品清單
            tempVendorProductEntities = getVendorProducts(tempItemVendorProductEntities, vendorProductEntities);
            if(null == tempVendorProductEntities || tempVendorProductEntities.isEmpty()){
                continue;
            }
            costPrice = getVendorProductCostPrice(tempItemVendorProductEntities, tempVendorProductEntities);
            quoteDetailEntity = new QuoteDetailEntity();
            quoteDetailEntity.setQuoteUuid(quoteEntity.getUuid());
            quoteDetailEntity.setItemUuid(itemEntity.getUuid());
            quoteDetailEntity.setItemNo(itemEntity.getNo());
            quoteDetailEntity.setItemName(itemEntity.getName());
            quoteDetailEntity.setItemSpec(itemEntity.getSpec());
            quoteDetailEntity.setItemUnit(itemEntity.getUnit());
            quoteDetailEntity.setQuantity(item.quantity());
            quoteDetailEntity.setItemVendorProductPrice(itemEntity.getAmount().setScale(0, RoundingMode.HALF_UP));
            quoteDetailEntity.setItemVendorProductAmount(itemEntity.getAmount().multiply(new BigDecimal(item.quantity())).setScale(0, RoundingMode.HALF_UP));
            quoteDetailEntity.setItemVendorProductCustomPrice(item.customUnitPrice().setScale(0, RoundingMode.HALF_UP));
            quoteDetailEntity.setItemVendorProductCustomAmount(item.customUnitPrice().multiply(new BigDecimal(item.quantity())).setScale(0, RoundingMode.HALF_UP));
            quoteDetailEntity.setItemVendorProductCostPrice(costPrice.setScale(0, RoundingMode.HALF_UP));
            quoteDetailEntity.setItemVendorProductCostAmount(costPrice.multiply(new BigDecimal(item.quantity())).setScale(0, RoundingMode.HALF_UP));
            quoteDetailEntities.add(quoteDetailEntity);
        }
        return quoteDetailEntities;
    }

    // 取得廠商產品成本
    private BigDecimal getVendorProductCostPrice(
            List<ItemVendorProductEntity> itemVendorProductEntities,
            List<VendorProductEntity> vendorProductEntities){
        BigDecimal costPrice = new BigDecimal(0);
        VendorProductEntity vendorProductEntity;
        BigDecimal unitPrice;
        for(ItemVendorProductEntity itemVendorProductEntity : itemVendorProductEntities){
            vendorProductEntity = CommonUtil.getEntityByUuid(vendorProductEntities, itemVendorProductEntity.getVendorProductUuid());
            unitPrice = vendorProductEntity.getUnitPrice().multiply(new BigDecimal(itemVendorProductEntity.getQuantity()));
            costPrice = costPrice.add(unitPrice.setScale(0, RoundingMode.HALF_UP));
        }
        return costPrice;
    }

    // 取得品項廠商產品清單
    private List<ItemVendorProductEntity> getItemVendorProducts(
            List<ItemVendorProductEntity> itemVendorProductEntities,
            ItemEntity itemEntity){
        List<ItemVendorProductEntity> result = new ArrayList<>();
        for(ItemVendorProductEntity itemVendorProductEntity : itemVendorProductEntities){
            if(!itemVendorProductEntity.getItemUuid().equals(itemEntity.getUuid())){
                continue;
            }
            result.add(itemVendorProductEntity);
        }
        return result;
    }

    // 取得廠商產品清單
    private List<VendorProductEntity> getVendorProducts(
            List<ItemVendorProductEntity> itemVendorProductEntities,
            List<VendorProductEntity> vendorProductEntities){
        List<VendorProductEntity> result = new ArrayList<>();
        VendorProductEntity vendorProductEntity;
        for(ItemVendorProductEntity itemVendorProductEntity : itemVendorProductEntities){
            // 取得廠商產品
            vendorProductEntity = CommonUtil.getEntityByUuid(vendorProductEntities, itemVendorProductEntity.getVendorProductUuid());
            if(null == vendorProductEntity){
                continue;
            }
            result.add(vendorProductEntity);
        }
        return result;
    }

    // 更新報價單金額
    private QuoteEntity updateQuoteAmount(QuoteEntity quoteEntity, List<QuoteDetailEntity> quoteDetailEntities){
        if(null == quoteDetailEntities || quoteDetailEntities.isEmpty()){
            return quoteEntity;
        }
        BigDecimal amount = getAmount(quoteDetailEntities);
        BigDecimal tax = amount.multiply(new BigDecimal(0.05)).setScale(0, RoundingMode.HALF_UP);
        BigDecimal totalAmount = amount.add(tax);
        BigDecimal customAmount = getCustomAmount(quoteDetailEntities);
        BigDecimal customTax = customAmount.multiply(new BigDecimal(0.05)).setScale(0, RoundingMode.HALF_UP);
        BigDecimal customTotalAmount = customAmount.add(customTax);
        BigDecimal costAmount = getCostAmount(quoteDetailEntities);
        BigDecimal costTax = costAmount.multiply(new BigDecimal(0.05)).setScale(0, RoundingMode.HALF_UP);
        BigDecimal costTotalAmount = costAmount.add(costTax);
        quoteEntity.setAmount(amount);
        quoteEntity.setTax(tax);
        quoteEntity.setTotalAmount(totalAmount);
        quoteEntity.setCustomAmount(customAmount);
        quoteEntity.setCustomTax(customTax);
        quoteEntity.setCustomTotalAmount(customTotalAmount);
        quoteEntity.setCostAmount(costAmount);
        quoteEntity.setCostTax(costTax);
        quoteEntity.setCostTotalAmount(costTotalAmount);
        return quoteEntity;
    }

    // 取得單位金額總計
    private BigDecimal getAmount(List<QuoteDetailEntity> quoteDetailEntities){
        BigDecimal unitPrice = new BigDecimal(0);
        for(QuoteDetailEntity quoteDetailEntity : quoteDetailEntities){
            unitPrice = unitPrice.add(quoteDetailEntity.getItemVendorProductAmount());
        }
        return unitPrice.setScale(0, RoundingMode.HALF_UP);
    }

    // 取得客製化總計
    private BigDecimal getCustomAmount(List<QuoteDetailEntity> quoteDetailEntities){
        BigDecimal customPrice = new BigDecimal(0);
        for(QuoteDetailEntity quoteDetailEntity : quoteDetailEntities){
            customPrice = customPrice.add(quoteDetailEntity.getItemVendorProductCustomAmount());
        }
        return customPrice.setScale(0, RoundingMode.HALF_UP);
    }

    // 取得客製化總計
    private BigDecimal getCostAmount(List<QuoteDetailEntity> quoteDetailEntities){
        BigDecimal costPrice = new BigDecimal(0);
        for(QuoteDetailEntity quoteDetailEntity : quoteDetailEntities){
            costPrice = costPrice.add(quoteDetailEntity.getItemVendorProductCostAmount());
        }
        return costPrice.setScale(0, RoundingMode.HALF_UP);
    }

    // 初始化廠商出貨清單
    private List<VendorQuoteEntity> initVendorQuotes(
            List<ItemVendorProductEntity> itemVendorProductEntities,
            QuoteEntity quoteEntity){
        List<VendorQuoteEntity> vendorQuoteEntities = new ArrayList<>();
        List<UUID> vendorUuids = getVendorUuids(itemVendorProductEntities);
        VendorQuoteEntity vendorQuoteEntity;
        for(UUID vendorUuid : vendorUuids){
            vendorQuoteEntity = new VendorQuoteEntity();
            vendorQuoteEntity.setUuid(UUID.randomUUID());
            vendorQuoteEntity.setVendorUuid(vendorUuid);
            vendorQuoteEntity.setQuoteUuid(quoteEntity.getUuid());
            vendorQuoteEntity.setCustomerUuid(quoteEntity.getCustomerUuid());
            vendorQuoteEntities.add(vendorQuoteEntity);
        }
        return vendorQuoteEntities;
    }

    // 取得廠商uuid
    private List<UUID> getVendorUuids(List<ItemVendorProductEntity> itemVendorProductEntities){
        LinkedHashSet<UUID> vendorUuids = new LinkedHashSet<>();
        if(null == itemVendorProductEntities || itemVendorProductEntities.isEmpty()){
            return new ArrayList<>(vendorUuids);
        }
        for(ItemVendorProductEntity itemVendorProductEntity : itemVendorProductEntities){
            vendorUuids.add(itemVendorProductEntity.getVendorUuid());
        }
        return new ArrayList<>(vendorUuids);
    }

    // 初始化廠商報價單明細清單
    private List<VendorQuoteDetailEntity> initVendorQuoteDetails(
            List<ItemVendorProductEntity> itemVendorProductEntities,
            List<VendorProductEntity> vendorProductEntities,
            List<VendorQuoteEntity> vendorQuoteEntities,
            QuoteCreateRequest request){
        List<VendorQuoteDetailEntity> vendorQuoteDetailEntities = new ArrayList<>();
        if(null == itemVendorProductEntities || itemVendorProductEntities.isEmpty()){
            return vendorQuoteDetailEntities;
        }
        if(null == vendorQuoteEntities || vendorQuoteEntities.isEmpty()){
            return vendorQuoteDetailEntities;
        }
        List<VendorQuoteDetailEntity> tempVendorQuoteDetailEntities;
        List<ItemVendorProductEntity> tempItemVendorProductEntities;
        for(VendorQuoteEntity vendorQuoteEntity : vendorQuoteEntities){
            // 取得品像廠商產品清單
            tempItemVendorProductEntities = getItemVendorProducts(itemVendorProductEntities, vendorQuoteEntity);
            // 初始化廠商報價單明細清單
            tempVendorQuoteDetailEntities = getVendorQuoteDetails(
                    tempItemVendorProductEntities,
                    vendorProductEntities,
                    vendorQuoteEntity,
                    request
            );
            vendorQuoteDetailEntities.addAll(tempVendorQuoteDetailEntities);
        }
        return vendorQuoteDetailEntities;
    }

    // 取得品像廠商產品清單
    private List<ItemVendorProductEntity> getItemVendorProducts(
            List<ItemVendorProductEntity> itemVendorProductEntities,
            VendorQuoteEntity vendorQuoteEntity){
        List<ItemVendorProductEntity> result = new ArrayList<>();
        if(null == itemVendorProductEntities || itemVendorProductEntities.isEmpty()){
            return result;
        }
        for(ItemVendorProductEntity itemVendorProductEntity : itemVendorProductEntities){
            if(itemVendorProductEntity.getVendorUuid().equals(vendorQuoteEntity.getVendorUuid())){
                result.add(itemVendorProductEntity);
            }
        }
        return result;
    }

    // 初始化廠商報價單明細清單
    private List<VendorQuoteDetailEntity> getVendorQuoteDetails(
            List<ItemVendorProductEntity> itemVendorProductEntities,
            List<VendorProductEntity> vendorProductEntities,
            VendorQuoteEntity vendorQuoteEntity,
            QuoteCreateRequest request){
        List<VendorQuoteDetailEntity> vendorQuoteDetailEntities = new ArrayList<>();
        if(null == itemVendorProductEntities || itemVendorProductEntities.isEmpty()){
            return vendorQuoteDetailEntities;
        }
        VendorQuoteDetailEntity vendorQuoteDetailEntity;
        VendorProductEntity vendorProductEntity;
        QuoteCreateRequest.Item item;
        BigDecimal unitPrice;
        BigDecimal vendorProductAmount;
        BigDecimal vendorProductTotalAmount;
        for(ItemVendorProductEntity itemVendorProductEntity : itemVendorProductEntities){
            vendorProductEntity = CommonUtil.getEntityByUuid(vendorProductEntities, itemVendorProductEntity.getVendorProductUuid());
            if(null == vendorProductEntity){
                continue;
            }
            item = getItem(request.items(), itemVendorProductEntity.getItemUuid());
            if(null == item){
                continue;
            }
            unitPrice = vendorProductEntity.getUnitPrice().setScale(0, RoundingMode.HALF_UP);
            vendorProductAmount = unitPrice.multiply(new BigDecimal(itemVendorProductEntity.getQuantity())).setScale(0, RoundingMode.HALF_UP);
            vendorProductTotalAmount = vendorProductAmount.multiply(new BigDecimal(item.quantity())).setScale(0, RoundingMode.HALF_UP);
            vendorQuoteDetailEntity = new VendorQuoteDetailEntity();
            vendorQuoteDetailEntity.setVendorQuoteUuid(vendorQuoteEntity.getUuid());
            vendorQuoteDetailEntity.setItemUuid(itemVendorProductEntity.getItemUuid());
            vendorQuoteDetailEntity.setVendorProductUuid(vendorProductEntity.getUuid());
            vendorQuoteDetailEntity.setVendorProductUnitPrice(unitPrice);
            vendorQuoteDetailEntity.setQuantity(itemVendorProductEntity.getQuantity());
            vendorQuoteDetailEntity.setVendorProductAmount(vendorProductAmount);
            vendorQuoteDetailEntity.setProductQuantity(item.quantity());
            vendorQuoteDetailEntity.setVendorProductTotalAmount(vendorProductTotalAmount);
            vendorQuoteDetailEntities.add(vendorQuoteDetailEntity);
        }
        return vendorQuoteDetailEntities;
    }

    // 取得項目
    private QuoteCreateRequest.Item getItem(List<QuoteCreateRequest.Item> items, UUID itemUuid){
        if(null == items || items.isEmpty()){
            return null;
        }
        for(QuoteCreateRequest.Item item : items){
            if(item.itemUuid().equals(itemUuid)){
                return item;
            }
        }
        return null;
    }

    // 更新報價單清單
    private List<VendorQuoteEntity> updateVendorQuotes(
            List<VendorQuoteEntity> vendorQuoteEntities,
            List<VendorQuoteDetailEntity> vendorQuoteDetailEntities){
        BigDecimal totalAmount;
        for(VendorQuoteEntity vendorQuoteEntity : vendorQuoteEntities){
            totalAmount = getTotalAmount(vendorQuoteEntity, vendorQuoteDetailEntities);
            vendorQuoteEntity.setTotalAmount(totalAmount.setScale(0, RoundingMode.HALF_UP));
        }
        return vendorQuoteEntities;
    }

    // 取得廠商報價單總金額
    private BigDecimal getTotalAmount(VendorQuoteEntity vendorQuoteEntity, List<VendorQuoteDetailEntity> vendorQuoteDetailEntities){
        BigDecimal totalAmount = new BigDecimal(0);
        for(VendorQuoteDetailEntity vendorQuoteDetailEntity : vendorQuoteDetailEntities){
            if(vendorQuoteDetailEntity.getVendorQuoteUuid().equals(vendorQuoteEntity.getUuid())){
                totalAmount = totalAmount.add(vendorQuoteDetailEntity.getVendorProductTotalAmount().setScale(0, RoundingMode.HALF_UP));
            }
        }
        return totalAmount.setScale(0, RoundingMode.HALF_UP);
    }

}
