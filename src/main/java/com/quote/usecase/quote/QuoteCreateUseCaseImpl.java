package com.quote.usecase.quote;

import com.quote.controller.quote.request.QuoteCreateRequest;
import com.quote.entity.customer.CustomerEntity;
import com.quote.entity.product.ProductEntity;
import com.quote.entity.quote.QuoteEntity;
import com.quote.entity.quote_detail.QuoteDetailEntity;
import com.quote.entity.user.UserEntity;
import com.quote.entity.vendor.VendorEntity;
import com.quote.service.customer.CustomerService;
import com.quote.service.product.ProductService;
import com.quote.service.quote.QuoteService;
import com.quote.service.quote_detail.QuoteDetailService;
import com.quote.service.user.UserService;
import com.quote.service.vendor.VendorService;
import com.quote.utils.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuoteCreateUseCaseImpl implements QuoteCreateUseCase {

    private final UserService userService;

    private final CustomerService customerService;

    private final QuoteService quoteService;

    private final QuoteDetailService quoteDetailService;

    private final ProductService productService;

    private final VendorService vendorService;

    @Override
    public void create(QuoteCreateRequest request) {
        List<QuoteCreateRequest.Product> products = request.products();
        UserEntity userEntity = userService.findByUuid(request.userUuid());
        CustomerEntity customerEntity = customerService.findByUuid(request.customerUuid());
        QuoteEntity quoteEntity = new QuoteEntity();
        quoteEntity.setUserUuid(userEntity.getUuid());
        quoteEntity.setUserName(userEntity.getName());
        quoteEntity.setCustomerUuid(customerEntity.getUuid());
        quoteEntity.setCustomerName(customerEntity.getName());
        quoteEntity.setCustomerAddress(customerEntity.getAddress());
        quoteEntity.setCustomerVatNumber(customerEntity.getVatNumber());
        quoteEntity.setHandleStaffName(request.handleStaffName());
        quoteEntity.setHandleStaffMobile(request.handleStaffMobile());
        // 建立報價單
        quoteEntity = quoteService.create(quoteEntity, JwtUtil.extractUsername());
        // 建立報價單明細
        List<QuoteDetailEntity> quoteDetailEntities = getQuoteDetails(products, quoteEntity);
    }

    // 取得報價單明細清單
    private List<QuoteDetailEntity> getQuoteDetails(
            List<QuoteCreateRequest.Product> products,
            QuoteEntity quoteEntity){
        List<VendorEntity> vendorEntities = vendorService.findAll();
        List<QuoteDetailEntity> quoteDetailEntities = new ArrayList<>();
        QuoteDetailEntity quoteDetailEntity;
        ProductEntity productEntity;
        VendorEntity vendorEntity;
        BigDecimal amount;
        BigDecimal costAmount;
        Integer index = 0;
        for(QuoteCreateRequest.Product product : products){
            productEntity = productService.findByUuid(product.productUuid());
            vendorEntity = getVendor(productEntity.getVendorUuid(), vendorEntities);
            quoteDetailEntity = new QuoteDetailEntity();
            quoteDetailEntity.setIndex(++index);
            quoteDetailEntity.setQuoteUuid(quoteEntity.getUuid());
            quoteDetailEntity.setProductUuid(productEntity.getUuid());
            quoteDetailEntity.setVoteUuid(vendorEntity.getUuid());
            quoteDetailEntity.setVoteName(vendorEntity.getName());
            quoteDetailEntity.setProductNo(productEntity.getNo());
            quoteDetailEntity.setProductName(productEntity.getName());
            quoteDetailEntity.setProductSpecification(productEntity.getSpecification());
            quoteDetailEntity.setProductUnitPrice(productEntity.getUnitPrice());
            quoteDetailEntity.setProductCustomUnitPrice(product.customUnitPrice());
            quoteDetailEntity.setProductCostPrice(productEntity.getCostPrice());
            quoteDetailEntity.setProductQuantity(product.quantity());
            amount = productEntity.getUnitPrice();
            amount = amount.multiply(new BigDecimal(product.quantity()));
            quoteDetailEntity.setProductAmount(amount);
            amount = amount.multiply(new BigDecimal(1.05));
            quoteDetailEntity.setProductAmountTax(amount);
            costAmount = product.customUnitPrice();
            costAmount = costAmount.multiply(new BigDecimal(product.quantity()));
            quoteDetailEntity.setProductCostAmount(costAmount);
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

}
