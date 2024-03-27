package com.design.usecase.quote;

import com.design.controller.quote.request.QuoteUpdateRequest;
import com.design.entity.customer.CustomerEntity;
import com.design.entity.enums.QuoteStatus;
import com.design.entity.item.ItemEntity;
import com.design.entity.product.ProductEntity;
import com.design.entity.quote.QuoteEntity;
import com.design.entity.quote_detail.QuoteDetailEntity;
import com.design.entity.user.UserEntity;
import com.design.entity.vendor.VendorEntity;
import com.design.service.customer.CustomerService;
import com.design.service.item.ItemService;
import com.design.service.product.ProductService;
import com.design.service.quote.QuoteService;
import com.design.service.quote_detail.QuoteDetailService;
import com.design.service.user.UserService;
import com.design.service.vendor.VendorService;
import com.design.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuoteUpdateUseCaseImpl implements QuoteUpdateUseCase {

    private final UserService userService;

    private final CustomerService customerService;

    private final QuoteService quoteService;

    private final QuoteDetailService quoteDetailService;

    private final ItemService itemService;

    private final ProductService productService;

    private final VendorService vendorService;

    @Override
    public void update(QuoteUpdateRequest request, String quoteUuid) {
        List<QuoteUpdateRequest.Product> products = request.products();
        UserEntity userEntity = userService.findByUuid(request.userUuid());
        CustomerEntity customerEntity = customerService.findByUuid(request.customerUuid());
        QuoteEntity quoteEntity = quoteService.findByUuid(quoteUuid);
        quoteEntity.setUserUuid(userEntity.getUuid());
        quoteEntity.setUserName(userEntity.getName());
        quoteEntity.setCustomerUuid(customerEntity.getUuid());
        quoteEntity.setCustomerName(customerEntity.getName());
        quoteEntity.setCustomerAddress(customerEntity.getAddress());
        quoteEntity.setCustomerVatNumber(customerEntity.getVatNumber());
        quoteEntity.setUnderTakerName(request.underTakerName());
        quoteEntity.setUnderTakerTel(request.underTakerTel());
        quoteEntity.setQuoteStatus(QuoteStatus.CREATE);
        // 取得舊的報價單明細清單
        List<QuoteDetailEntity> oldQuoteDetailEntities = quoteDetailService.findAll(quoteUuid);
        // 取得新的報價單明細清單
        List<QuoteDetailEntity> quoteDetailEntities = getQuoteDetails(products, quoteEntity);
        // 設定金額
        quoteEntity = setAmount(quoteDetailEntities, quoteEntity);
        // 建立報價單
        quoteService.create(quoteEntity, JwtUtil.extractUsername());
        // 刪除舊的報價單明細清單
        quoteDetailService.deleteAll(oldQuoteDetailEntities, JwtUtil.extractUsername());
        // 建立報價單明細清單
        quoteDetailService.createAll(quoteDetailEntities, JwtUtil.extractUsername());
    }

    // 取得報價單明細清單
    private List<QuoteDetailEntity> getQuoteDetails(
            List<QuoteUpdateRequest.Product> products,
            QuoteEntity quoteEntity){
        List<VendorEntity> vendorEntities = vendorService.findAll();
        List<QuoteDetailEntity> quoteDetailEntities = new ArrayList<>();
        QuoteDetailEntity quoteDetailEntity;
        ItemEntity itemEntity;
        ProductEntity productEntity;
        VendorEntity vendorEntity;
        BigDecimal amount;
        BigDecimal customAmount;
        BigDecimal costAmount;
        for(QuoteUpdateRequest.Product product : products){
            productEntity = productService.findByUuid(product.productUuid());
            amount = productEntity.getUnitPrice();
            amount = amount.multiply(new BigDecimal(product.quantity()));
            customAmount = product.customUnitPrice();
            customAmount = customAmount.multiply(new BigDecimal(product.quantity()));
            costAmount = productEntity.getCostPrice();
            costAmount = costAmount.multiply(new BigDecimal(product.quantity()));
            itemEntity = itemService.findByUuid(productEntity.getItemUuid());
            vendorEntity = getVendor(productEntity.getVendorUuid(), vendorEntities);
            quoteDetailEntity = new QuoteDetailEntity();
            quoteDetailEntity.setQuoteUuid(quoteEntity.getUuid());
            quoteDetailEntity.setVoteUuid(vendorEntity.getUuid());
            quoteDetailEntity.setVoteName(vendorEntity.getName());
            quoteDetailEntity.setProductNo(productEntity.getNo());
            quoteDetailEntity.setItemUuid(itemEntity.getUuid());
            quoteDetailEntity.setItemName(itemEntity.getName());
            quoteDetailEntity.setProductUuid(productEntity.getUuid());
            quoteDetailEntity.setProductUnit(productEntity.getUnit());
            quoteDetailEntity.setProductSpecification(productEntity.getSpecification());
            quoteDetailEntity.setProductUnitPrice(productEntity.getUnitPrice());
            quoteDetailEntity.setProductCustomUnitPrice(product.customUnitPrice());
            quoteDetailEntity.setProductCostPrice(productEntity.getCostPrice());
            quoteDetailEntity.setProductQuantity(product.quantity());
            quoteDetailEntity.setProductAmount(amount);
            quoteDetailEntity.setProductCustomAmount(customAmount);
            quoteDetailEntity.setProductCostAmount(costAmount);
            quoteDetailEntities.add(quoteDetailEntity);
        }
        return quoteDetailEntities;
    }

    // 取得廠商
    private VendorEntity getVendor(String vendorUuid, List<VendorEntity> vendorEntities){
        if(null == vendorEntities || vendorEntities.isEmpty()){
            return null;
        }
        for(VendorEntity vendorEntity : vendorEntities){
            if(vendorEntity.getUuid().equals(vendorUuid)){
                return vendorEntity;
            }
        }
        return null;
    }

    // 設定金額
    private QuoteEntity setAmount(List<QuoteDetailEntity> quoteDetailEntities, QuoteEntity quoteEntity){
        if(null == quoteDetailEntities || quoteDetailEntities.isEmpty()){
            return quoteEntity;
        }
        BigDecimal amount = new BigDecimal(0);
        BigDecimal tax = new BigDecimal(0);
        BigDecimal totalAmount = new BigDecimal(0);
        BigDecimal customAmount = new BigDecimal(0);
        BigDecimal customTax = new BigDecimal(0);
        BigDecimal customTotalAmount = new BigDecimal(0);
        BigDecimal costAmount = new BigDecimal(0);
        BigDecimal costTax = new BigDecimal(0);
        BigDecimal costTotalAmount = new BigDecimal(0);
        for(QuoteDetailEntity quoteDetailEntity : quoteDetailEntities){
            amount = amount.add(quoteDetailEntity.getProductAmount());
            customAmount = customAmount.add(quoteDetailEntity.getProductCustomAmount());
            costAmount = costAmount.add(quoteDetailEntity.getProductCostAmount());
        }
        tax = amount.multiply(new BigDecimal(0.05));
        tax = tax.setScale(0, RoundingMode.HALF_UP);
        totalAmount = totalAmount.add(amount);
        totalAmount = totalAmount.add(tax);

        customTax = customAmount.multiply(new BigDecimal(0.05));
        customTax = customTax.setScale(0, RoundingMode.HALF_UP);
        customTotalAmount = customTotalAmount.add(customAmount);
        customTotalAmount = customTotalAmount.add(customTax);

        costTax = costAmount.multiply(new BigDecimal(0.05));
        costTax = costTax.setScale(0, RoundingMode.HALF_UP);
        costTotalAmount = costTotalAmount.add(costAmount);
        costTotalAmount = costTotalAmount.add(costTax);

        quoteEntity.setAmount(amount);
        quoteEntity.setTax(tax);
        quoteEntity.setTotalAmount(totalAmount);
        quoteEntity.setCustomAmount(customAmount);
        quoteEntity.setCustomTax(customTax);
        quoteEntity.setCustomerTotalAmount(customTotalAmount);
        quoteEntity.setCostAmount(costAmount);
        quoteEntity.setCostTax(costTax);
        quoteEntity.setCostTotalAmount(costTotalAmount);
        return quoteEntity;
    }

}
