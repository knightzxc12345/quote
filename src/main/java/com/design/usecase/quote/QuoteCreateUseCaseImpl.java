package com.design.usecase.quote;

import com.design.controller.quote.request.QuoteCreateRequest;
import com.design.entity.customer.CustomerEntity;
import com.design.entity.enums.QuoteStatus;
import com.design.entity.enums.VendorQuoteStatus;
import com.design.entity.item.ItemEntity;
import com.design.entity.product.ProductEntity;
import com.design.entity.product_vendor.ProductVendorEntity;
import com.design.entity.quote.QuoteEntity;
import com.design.entity.quote_detail.QuoteDetailEntity;
import com.design.entity.user.UserEntity;
import com.design.entity.vendor.VendorEntity;
import com.design.entity.vendor_quote.VendorQuoteEntity;
import com.design.entity.vendor_quote_detail.VendorQuoteDetailEntity;
import com.design.service.customer.CustomerService;
import com.design.service.item.ItemService;
import com.design.service.product.ProductService;
import com.design.service.product_vendor.ProductVendorService;
import com.design.service.quote.QuoteService;
import com.design.service.quote_detail.QuoteDetailService;
import com.design.service.user.UserService;
import com.design.service.vendor.VendorService;
import com.design.service.vendor_quote.VendorQuoteService;
import com.design.service.vendor_quote_detail.VendorQuoteDetailService;
import com.design.utils.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class QuoteCreateUseCaseImpl implements QuoteCreateUseCase {

    private final UserService userService;

    private final CustomerService customerService;

    private final QuoteService quoteService;

    private final QuoteDetailService quoteDetailService;

    private final ProductService productService;

    private final ProductVendorService productVendorService;

    private final ItemService itemService;

    private final VendorService vendorService;

    private final VendorQuoteService vendorQuoteService;

    private final VendorQuoteDetailService vendorQuoteDetailService;

    @Override
    public void create(QuoteCreateRequest request) {
        // 取得使用者名稱
        String userName = JwtUtil.extractUsername();
        List<QuoteCreateRequest.Product> products = request.products();
        // 取得產品uuid清單
        List<String> productUuids = getProductUuids(products);
        // 取得產品清單
        List<ProductEntity> productEntities = productService.findAllByProductUuidIn(productUuids);
        // 取得項目uuid清單
        List<String> itemUuids = getItemUuids(productEntities);
        // 取得項目清單
        List<ItemEntity> itemEntities = itemService.findAllItemUuidIn(itemUuids);
        // 取得產品廠商清單
        List<ProductVendorEntity> productVendorEntities = productVendorService.findAllProductUuidIn(productUuids);
        // 取得廠商uuid清單
        List<String> vendorUuids = getVendorsUuids(productVendorEntities);
        // 取得廠商清單
        List<VendorEntity> vendorEntities = vendorService.findAllVendorUuidIn(vendorUuids);
        // 取得使用者
        UserEntity userEntity = userService.findByUuid(request.userUuid());
        // 取得客戶
        CustomerEntity customerEntity = customerService.findByUuid(request.customerUuid());
        // 初始化報價單
        QuoteEntity quoteEntity = initQuote(request, userEntity, customerEntity);
        // 初始化報價單明細清單
        List<QuoteDetailEntity> quoteDetailEntities = initQuoteDetails(products, productEntities, itemEntities, quoteEntity);
        // 設定金額
        quoteEntity = setAmount(quoteDetailEntities, quoteEntity);
        // 初始化廠商報價單
        List<VendorQuoteEntity> vendorQuoteEntities = initVendorQuotes(vendorEntities, quoteEntity, customerEntity);
        // 初始化廠商報價明細清單
        List<VendorQuoteDetailEntity> vendorQuoteDetailEntities = initVendorQuoteDetails(vendorEntities, vendorQuoteEntities, quoteDetailEntities);
        // 建立報價單
        quoteService.create(quoteEntity, userName);
        // 建立報價單明細清單
        quoteDetailService.createAll(quoteDetailEntities, userName);
        // 建立廠商報價單
        vendorQuoteService.createAll(vendorQuoteEntities, userName);
        // 建立廠商報價單明細清單
        vendorQuoteDetailService.createAll(vendorQuoteDetailEntities, userName);
    }

    // 初始化報價單
    private QuoteEntity initQuote(QuoteCreateRequest request, UserEntity userEntity, CustomerEntity customerEntity){
        QuoteEntity quoteEntity = new QuoteEntity();
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
    private List<QuoteDetailEntity> initQuoteDetails(
            List<QuoteCreateRequest.Product> products,
            List<ProductEntity> productEntities,
            List<ItemEntity> itemEntities,
            QuoteEntity quoteEntity){
        List<QuoteDetailEntity> quoteDetailEntities = new ArrayList<>();
        QuoteDetailEntity quoteDetailEntity;
        ProductEntity productEntity;
        ItemEntity itemEntity;
        BigDecimal amount;
        BigDecimal customAmount;
        BigDecimal costAmount;
        for(QuoteCreateRequest.Product product : products){
            productEntity = getProduct(productEntities, product.productUuid());
            itemEntity = getItem(itemEntities, productEntity.getItemUuid());
            amount = productEntity.getUnitPrice();
            amount = amount.multiply(new BigDecimal(product.quantity()));
            customAmount = product.customUnitPrice();
            customAmount = customAmount.multiply(new BigDecimal(product.quantity()));
            costAmount = productEntity.getCostPrice();
            costAmount = costAmount.multiply(new BigDecimal(product.quantity()));
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
            quoteDetailEntity.setProductCostPrice(productEntity.getCostPrice());
            quoteDetailEntity.setProductQuantity(product.quantity());
            quoteDetailEntity.setProductAmount(amount);
            quoteDetailEntity.setProductCustomAmount(customAmount);
            quoteDetailEntity.setProductCostAmount(costAmount);
            quoteDetailEntities.add(quoteDetailEntity);
        }
        return quoteDetailEntities;
    }

    // 取得產品uuid清單
    private List<String> getProductUuids(List<QuoteCreateRequest.Product> products){
        List<String> productUuids = new ArrayList<>();
        if(null == products || products.isEmpty()){
            return productUuids;
        }
        for(QuoteCreateRequest.Product product : products){
            productUuids.add(product.productUuid());
        }
        return productUuids;
    }

    // 取得產品
    private ProductEntity getProduct(List<ProductEntity> productEntities, String productUuid){
        if(null == productEntities || productEntities.isEmpty()){
            return null;
        }
        for(ProductEntity productEntity : productEntities){
            if(productEntity.getUuid().equals(productUuid)){
                return productEntity;
            }
        }
        return null;
    }

    // 取得項目uuid清單
    private List<String> getItemUuids(List<ProductEntity> productEntities){
        Set<String> itemUuids = new HashSet<>();
        if(null == productEntities || productEntities.isEmpty()){
            return new ArrayList<>();
        }
        for(ProductEntity productEntity : productEntities){
            itemUuids.add(productEntity.getItemUuid());
        }
        return new ArrayList<>(itemUuids);
    }

    // 取得項目
    private ItemEntity getItem(List<ItemEntity> itemEntities, String itemUuid){
        if(null == itemEntities || itemEntities.isEmpty()){
            return null;
        }
        for(ItemEntity itemEntity : itemEntities){
            if(itemEntity.getUuid().equals(itemUuid)){
                return itemEntity;
            }
        }
        return null;
    }

    // 取得廠商uuid清單
    private List<String> getVendorsUuids(List<ProductVendorEntity> productVendorEntities){
        Set<String> vendorUuids = new HashSet<>();
        if(null == productVendorEntities || productVendorEntities.isEmpty()){
            return new ArrayList<>();
        }
        for(ProductVendorEntity productVendorEntity : productVendorEntities){
            vendorUuids.add(productVendorEntity.getVendorUuid());
        }
        return new ArrayList<>(vendorUuids);
    }

    // 取得廠商
    private VendorEntity getVendor(List<VendorEntity> vendorEntities, String productUuid){
        if(null == vendorEntities || vendorEntities.isEmpty()){
            return null;
        }
        for(VendorEntity vendorEntity : vendorEntities){
            if(vendorEntity.getUuid().equals(productUuid)){
                return vendorEntity;
            }
        }
        return null;
    }

    // 初始化廠商報價單
    private List<VendorQuoteEntity> initVendorQuotes(
            List<VendorEntity> vendorEntities,
            QuoteEntity quoteEntity,
            CustomerEntity customerEntity) {
        List<VendorQuoteEntity> vendorQuoteEntities = new ArrayList<>();
        if (null == vendorEntities || vendorEntities.isEmpty()) {
            return vendorQuoteEntities;
        }
        VendorQuoteEntity vendorQuoteEntity;
        for (VendorEntity vendorEntity : vendorEntities) {
            vendorQuoteEntity = new VendorQuoteEntity();
            vendorQuoteEntity.setUuid(UUID.randomUUID().toString());
            vendorQuoteEntity.setVendorUuid(vendorEntity.getUuid());
            vendorQuoteEntity.setQuoteUuid(quoteEntity.getUuid());
            vendorQuoteEntity.setCustomerUuid(customerEntity.getUuid());
            vendorQuoteEntity.setVendorQuoteStatus(VendorQuoteStatus.CREATE);
        }
        return vendorQuoteEntities;
    }

    // 初始化廠商報價明細清單
    private List<VendorQuoteDetailEntity> initVendorQuoteDetails(
            List<VendorEntity> vendorEntities,
            List<VendorQuoteEntity> vendorQuoteEntities,
            List<QuoteDetailEntity> quoteDetailEntities){
        List<VendorQuoteDetailEntity> vendorQuoteDetailEntities = new ArrayList<>();
        if(null == quoteDetailEntities || quoteDetailEntities.isEmpty()){
            return vendorQuoteDetailEntities;
        }
        VendorEntity vendorEntity;
        VendorQuoteEntity vendorQuoteEntity;
        VendorQuoteDetailEntity vendorQuoteDetailEntity;
        for(QuoteDetailEntity quoteDetailEntity : quoteDetailEntities){
            // 取得廠商
            vendorEntity = getVendor(vendorEntities, quoteDetailEntity.getProductUuid());
            // 取得廠商報價
            vendorQuoteEntity = getVendorQuote(vendorQuoteEntities, vendorEntity.getUuid());
            vendorQuoteDetailEntity = new VendorQuoteDetailEntity();
            vendorQuoteDetailEntity.setVendorQuoteUuid(vendorQuoteEntity.getQuoteUuid());
            vendorQuoteDetailEntity.setItemUuid(quoteDetailEntity.getItemUuid());
            vendorQuoteDetailEntity.setItemName(quoteDetailEntity.getItemName());
            vendorQuoteDetailEntity.setItemNo(quoteDetailEntity.getItemNo());
            vendorQuoteDetailEntity.setProductUuid(quoteDetailEntity.getProductUuid());
            vendorQuoteDetailEntity.setProductSpecification(quoteDetailEntity.getProductSpecification());
            vendorQuoteDetailEntity.setProductUnit(quoteDetailEntity.getProductUnit());
            vendorQuoteDetailEntity.setProductUnitPrice(quoteDetailEntity.getProductCostPrice());
            vendorQuoteDetailEntity.setProductQuantity(quoteDetailEntity.getProductQuantity());
            vendorQuoteDetailEntity.setProductAmount(quoteDetailEntity.getProductCostAmount());
            vendorQuoteDetailEntities.add(vendorQuoteDetailEntity);
        }
        return vendorQuoteDetailEntities;
    }

    // 取得廠商報價
    private VendorQuoteEntity getVendorQuote(
            List<VendorQuoteEntity> vendorQuoteEntities,
            String vendorUuid){
        if(null == vendorQuoteEntities || vendorQuoteEntities.isEmpty()){
            return null;
        }
        for(VendorQuoteEntity vendorQuoteEntity : vendorQuoteEntities){
            if(vendorQuoteEntity.getVendorUuid().equals(vendorUuid)){
                return vendorQuoteEntity;
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
        quoteEntity.setCustomTotalAmount(customTotalAmount);
        quoteEntity.setCostAmount(costAmount);
        quoteEntity.setCostTax(costTax);
        quoteEntity.setCostTotalAmount(costTotalAmount);
        return quoteEntity;
    }

}
