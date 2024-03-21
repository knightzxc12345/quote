package com.design.usecase.quote;

import com.design.controller.quote.request.QuoteCreateRequest;
import com.design.entity.customer.CustomerEntity;
import com.design.entity.enums.QuoteStatus;
import com.design.entity.product.ProductEntity;
import com.design.entity.quote.QuoteEntity;
import com.design.entity.quote_detail.QuoteDetailEntity;
import com.design.entity.user.UserEntity;
import com.design.entity.vendor.VendorEntity;
import com.design.service.customer.CustomerService;
import com.design.service.product.ProductService;
import com.design.service.quote.QuoteService;
import com.design.service.quote_detail.QuoteDetailService;
import com.design.service.user.UserService;
import com.design.service.vendor.VendorService;
import com.design.utils.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        quoteEntity.setQuoteStatus(QuoteStatus.CREATE);
        // 建立報價單
        quoteEntity = quoteService.create(quoteEntity, JwtUtil.extractUsername());
        // 建立報價單明細
        List<QuoteDetailEntity> quoteDetailEntities = getQuoteDetails(products, quoteEntity);
        quoteDetailService.createAll(quoteDetailEntities, JwtUtil.extractUsername());
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
        for(QuoteCreateRequest.Product product : products){
            productEntity = productService.findByUuid(product.productUuid());
            vendorEntity = getVendor(productEntity.getVendorUuid(), vendorEntities);
            quoteDetailEntity = new QuoteDetailEntity();
            quoteDetailEntity.setQuoteUuid(quoteEntity.getUuid());
            quoteDetailEntity.setProductUuid(productEntity.getUuid());
            quoteDetailEntity.setVoteUuid(vendorEntity.getUuid());
            quoteDetailEntity.setVoteName(vendorEntity.getName());
            quoteDetailEntity.setProductNo(productEntity.getNo());
//            quoteDetailEntity.setProductName(productEntity.getName());
            quoteDetailEntity.setProductSpecification(productEntity.getSpecification());
            quoteDetailEntity.setProductUnitPrice(productEntity.getUnitPrice());
            quoteDetailEntity.setProductCustomUnitPrice(product.customUnitPrice());
            quoteDetailEntity.setProductCostPrice(productEntity.getCostPrice());
            quoteDetailEntity.setProductQuantity(product.quantity());
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

}
