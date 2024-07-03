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
    public void update(QuoteUpdateRequest request, UUID quoteUuid) {
        List<QuoteUpdateRequest.Product> products = request.products();
        // 取得使用者
        UserEntity userEntity = userService.findByUuid(request.userUuid());
        // 取得客戶
        CustomerEntity customerEntity = customerService.findByUuid(request.customerUuid());
        // 取得報價單
        QuoteEntity quoteEntity = quoteService.findByUuid(quoteUuid);
        quoteEntity = update(quoteEntity, userEntity, customerEntity, request);
        // 取得舊的報價單明細清單
        List<QuoteDetailEntity> oldQuoteDetailEntities = quoteDetailService.findAll(quoteUuid);
        // 取得新的報價單明細清單
        List<QuoteDetailEntity> quoteDetailEntities = getQuoteDetails(products, quoteEntity);
        // 設定金額
        quoteEntity = setAmount(quoteDetailEntities, quoteEntity);
        // 建立報價單
        quoteService.create(quoteEntity, JwtUtil.extractUserUuid());
        // 刪除舊的報價單明細清單
        quoteDetailService.deleteAll(oldQuoteDetailEntities, JwtUtil.extractUserUuid());
        // 建立報價單明細清單
        quoteDetailService.createAll(quoteDetailEntities, JwtUtil.extractUserUuid());
    }

    private QuoteEntity update(
            QuoteEntity quoteEntity,
            UserEntity userEntity,
            CustomerEntity customerEntity,
            QuoteUpdateRequest request){
        quoteEntity.setUserUuid(userEntity.getUuid());
        quoteEntity.setUserName(userEntity.getName());
        quoteEntity.setCustomerUuid(customerEntity.getUuid());
        quoteEntity.setCustomerName(customerEntity.getName());
        quoteEntity.setCustomerAddress(customerEntity.getAddress());
        quoteEntity.setCustomerVatNumber(customerEntity.getVatNumber());
        quoteEntity.setUnderTakerName(request.underTakerName());
        quoteEntity.setUnderTakerTel(request.underTakerTel());
        quoteEntity.setQuoteStatus(QuoteStatus.CREATE);
        return quoteEntity;
    }

    // 取得報價單明細清單
    private List<QuoteDetailEntity> getQuoteDetails(
            List<QuoteUpdateRequest.Product> products,
            QuoteEntity quoteEntity){
        List<QuoteDetailEntity> quoteDetailEntities = new ArrayList<>();
        QuoteDetailEntity quoteDetailEntity;
        ItemEntity itemEntity;
        ProductEntity productEntity;
        BigDecimal amount;
        BigDecimal customAmount;
        for(QuoteUpdateRequest.Product product : products){
            productEntity = productService.findByUuid(product.productUuid());
            itemEntity = itemService.findByUuid(productEntity.getItemUuid());
            amount = productEntity.getUnitPrice();
            amount = amount.multiply(new BigDecimal(product.quantity()));
            customAmount = product.customUnitPrice();
            customAmount = customAmount.multiply(new BigDecimal(product.quantity()));
            quoteDetailEntity = new QuoteDetailEntity();
            quoteDetailEntity.setQuoteUuid(quoteEntity.getUuid());
            quoteDetailEntity.setItemUuid(itemEntity.getUuid());
            quoteDetailEntity.setItemNo(itemEntity.getNo());
            quoteDetailEntity.setItemName(itemEntity.getName());
            quoteDetailEntity.setProductUuid(productEntity.getUuid());
            quoteDetailEntity.setProductUnit(productEntity.getUnit());
            quoteDetailEntity.setProductSpecification(productEntity.getSpecification());
            quoteDetailEntity.setProductUnitPrice(productEntity.getUnitPrice());
            quoteDetailEntity.setProductCustomUnitPrice(product.customUnitPrice());
            quoteDetailEntity.setProductQuantity(product.quantity());
            quoteDetailEntity.setProductAmount(amount);
            quoteDetailEntity.setProductCustomAmount(customAmount);
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
        for(QuoteDetailEntity quoteDetailEntity : quoteDetailEntities){
            amount = amount.add(quoteDetailEntity.getProductAmount());
            customAmount = customAmount.add(quoteDetailEntity.getProductCustomAmount());
        }
        tax = amount.multiply(new BigDecimal(0.05));
        tax = tax.setScale(0, RoundingMode.HALF_UP);
        totalAmount = totalAmount.add(amount);
        totalAmount = totalAmount.add(tax);

        customTax = customAmount.multiply(new BigDecimal(0.05));
        customTax = customTax.setScale(0, RoundingMode.HALF_UP);
        customTotalAmount = customTotalAmount.add(customAmount);
        customTotalAmount = customTotalAmount.add(customTax);

        quoteEntity.setAmount(amount);
        quoteEntity.setTax(tax);
        quoteEntity.setTotalAmount(totalAmount);
        quoteEntity.setCustomAmount(customAmount);
        quoteEntity.setCustomTax(customTax);
        quoteEntity.setCustomTotalAmount(customTotalAmount);
        return quoteEntity;
    }

}
