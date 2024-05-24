package com.design.usecase.vendor_quote;

import com.design.controller.vendor_quote.request.VendorQuoteRequest;
import com.design.controller.vendor_quote.response.VendorQuoteFindAllResponse;
import com.design.controller.vendor_quote.response.VendorQuoteFindPageResponse;
import com.design.controller.vendor_quote.response.VendorQuoteFindResponse;
import com.design.entity.vendor_quote.VendorQuoteEntity;
import com.design.entity.vendor_quote_detail.VendorQuoteDetailEntity;
import com.design.service.vendor_quote.VendorQuoteService;
import com.design.service.vendor_quote_detail.VendorQuoteDetailService;
import com.design.utils.InstantUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorQuoteFindUseCaseImpl implements VendorQuoteFindUseCase {

    private final VendorQuoteService vendorQuoteService;

    private final VendorQuoteDetailService vendorQuoteDetailService;

    @Override
    public VendorQuoteFindResponse findByUuid(String vendorQuoteUuid) {
        // 取得廠商報價單
        VendorQuoteEntity vendorQuoteEntity = vendorQuoteService.findByUuid(vendorQuoteUuid);
        // 取得廠商報價單明細清單
        List<VendorQuoteDetailEntity> vendorQuoteDetailEntities = vendorQuoteDetailService.findAllByVendorQuoteUuid(vendorQuoteUuid);
        // 取得廠商報價單明細回傳清單
        List<VendorQuoteFindResponse.Product> products = getProducts(vendorQuoteDetailEntities);
        return new VendorQuoteFindResponse(
                vendorQuoteEntity.getVendorUuid(),
                vendorQuoteEntity.getCustomerUuid(),
                vendorQuoteEntity.getVendorQuoteStatus().get(),
                products
        );
    }

    private List<VendorQuoteFindResponse.Product> getProducts(List<VendorQuoteDetailEntity> vendorQuoteDetailEntities){
        List<VendorQuoteFindResponse.Product> products = new ArrayList<>();
        if(null == vendorQuoteDetailEntities || vendorQuoteDetailEntities.isEmpty()){
            return products;
        }
        for(VendorQuoteDetailEntity vendorQuoteDetailEntity : vendorQuoteDetailEntities){
            products.add(new VendorQuoteFindResponse.Product(
                    vendorQuoteDetailEntity.getItemUuid(),
                    vendorQuoteDetailEntity.getProductUuid(),
                    vendorQuoteDetailEntity.getProductQuantity()
            ));
        }
        return products;
    }

    @Override
    public List<VendorQuoteFindAllResponse> findAll(VendorQuoteRequest request) {
        List<VendorQuoteEntity> vendorQuoteEntities = vendorQuoteService.findAll(
                request.vendorUuid(),
                request.customerUuid()
        );
        return format(vendorQuoteEntities);
    }

    @Override
    public VendorQuoteFindPageResponse findAllByPage(VendorQuoteRequest request) {
        Page<VendorQuoteEntity> vendorQuoteEntityPage = vendorQuoteService.findAllByPage(
                request.vendorUuid(),
                request.customerUuid(),
                request.page(),
                request.size()
        );
        List<VendorQuoteFindAllResponse> responses = format(vendorQuoteEntityPage.getContent());
        return new VendorQuoteFindPageResponse(
                vendorQuoteEntityPage.getTotalPages(),
                vendorQuoteEntityPage.getNumber(),
                vendorQuoteEntityPage.getSize(),
                responses
        );
    }

    private List<VendorQuoteFindAllResponse> format(List<VendorQuoteEntity> vendorQuoteEntities){
        List<VendorQuoteFindAllResponse> responses = new ArrayList<>();
        if(null == vendorQuoteEntities || vendorQuoteEntities.isEmpty()){
            return responses;
        }
        for(VendorQuoteEntity vendorQuoteEntity : vendorQuoteEntities){
            responses.add(new VendorQuoteFindAllResponse(
                    vendorQuoteEntity.getQuoteUuid(),
                    vendorQuoteEntity.getVendorUuid(),
                    vendorQuoteEntity.getCustomerUuid(),
                    vendorQuoteEntity.getAmount(),
                    vendorQuoteEntity.getVendorQuoteStatus().getValue(),
                    InstantUtil.to(vendorQuoteEntity.getCreateTime())
            ));
        }
        return responses;
    }

}
