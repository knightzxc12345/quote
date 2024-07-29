package com.design.usecase.vendor_quote;

import com.design.controller.vendor_quote.request.VendorQuoteFindRequest;
import com.design.controller.vendor_quote.response.VendorQuoteFindAllResponse;
import com.design.controller.vendor_quote.response.VendorQuoteFindPageResponse;
import com.design.controller.vendor_quote.response.VendorQuoteFindResponse;
import com.design.entity.customer.CustomerEntity;
import com.design.entity.item.ItemEntity;
import com.design.entity.vendor.VendorEntity;
import com.design.entity.vendor_quote.VendorQuoteEntity;
import com.design.entity.vendor_quote_detail.VendorQuoteDetailEntity;
import com.design.service.customer.CustomerService;
import com.design.service.item.ItemService;
import com.design.service.vendor.VendorService;
import com.design.service.vendor_quote.VendorQuoteService;
import com.design.service.vendor_quote_detail.VendorQuoteDetailService;
import com.design.utils.CommonUtil;
import com.design.utils.InstantUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorQuoteFindUseCaseImpl implements VendorQuoteFindUseCase {

    private final VendorQuoteService vendorQuoteService;

    private final VendorQuoteDetailService vendorQuoteDetailService;

    private final VendorService vendorService;

    private final CustomerService customerService;

    private final ItemService itemService;

    @Override
    public VendorQuoteFindResponse findByUuid(UUID vendorQuoteUuid) {
        // 取得廠商報價單
        VendorQuoteEntity vendorQuoteEntity = vendorQuoteService.findByUuid(vendorQuoteUuid);
        // 取得廠商
        VendorEntity vendorEntity = vendorService.findByUuid(vendorQuoteEntity.getVendorUuid());
        // 取得客戶
        CustomerEntity customerEntity = customerService.findByUuid(vendorQuoteEntity.getCustomerUuid());
        // 取得廠商報價單明細清單
        List<VendorQuoteDetailEntity> vendorQuoteDetailEntities = vendorQuoteDetailService.findAllByVendorQuoteUuid(vendorQuoteUuid);
        // 取得廠商報價單明細回傳清單
        List<VendorQuoteFindResponse.Item> items = getItems(vendorQuoteDetailEntities);
        return new VendorQuoteFindResponse(
                vendorEntity.getName(),
                customerEntity.getName(),
                vendorQuoteEntity.getVendorQuoteStatus().get(),
                items
        );
    }

    @Override
    public List<VendorQuoteFindAllResponse> findAll(VendorQuoteFindRequest request) {
        String vendorUuid = null == request.vendorUuid() ? null : request.vendorUuid().toString();
        String customerUuid = null == request.customerUuid() ? null : request.customerUuid().toString();
        List<VendorQuoteEntity> vendorQuoteEntities = vendorQuoteService.findAll(
                vendorUuid,
                customerUuid
        );
        return format(vendorQuoteEntities);
    }

    @Override
    public VendorQuoteFindPageResponse findAllByPage(VendorQuoteFindRequest request) {
        String vendorUuid = null == request.vendorUuid() ? null : request.vendorUuid().toString();
        String customerUuid = null == request.customerUuid() ? null : request.customerUuid().toString();
        Page<VendorQuoteEntity> vendorQuoteEntityPage = vendorQuoteService.findAllByPage(
                vendorUuid,
                customerUuid,
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

    // 取得品項清單
    private List<VendorQuoteFindResponse.Item> getItems(List<VendorQuoteDetailEntity> vendorQuoteDetailEntities){
        List<VendorQuoteFindResponse.Item> items = new ArrayList<>();
        if(null == vendorQuoteDetailEntities || vendorQuoteDetailEntities.isEmpty()){
            return items;
        }
        // 取得品項uuid清單
        List<UUID> itemUuids = getItemUuids(vendorQuoteDetailEntities);
        // 取得品項清單
        List<ItemEntity> itemEntities = itemService.findAllItemUuidIn(itemUuids);
        if(null == itemEntities || itemEntities.isEmpty()){
            return items;
        }
        for(ItemEntity itemEntity : itemEntities){

        }
        return items;
    }

    // 取得品項uuid清單
    private List<UUID> getItemUuids(List<VendorQuoteDetailEntity> vendorQuoteDetailEntities){
        LinkedHashSet<UUID> itemUuids = new LinkedHashSet<>();
        if(null == vendorQuoteDetailEntities || vendorQuoteDetailEntities.isEmpty()){
            return new ArrayList<>(itemUuids);
        }
        for(VendorQuoteDetailEntity vendorQuoteDetailEntity : vendorQuoteDetailEntities){
            itemUuids.add(vendorQuoteDetailEntity.getItemUuid());
        }
        return new ArrayList<>(itemUuids);
    }

    private List<VendorQuoteFindAllResponse> format(List<VendorQuoteEntity> vendorQuoteEntities){
        List<VendorQuoteFindAllResponse> responses = new ArrayList<>();
        if(null == vendorQuoteEntities || vendorQuoteEntities.isEmpty()){
            return responses;
        }
        // 取得廠商uuid清單
        List<UUID> vendorUuids = getVendorUuids(vendorQuoteEntities);
        // 取得客戶uuid清單
        List<UUID> customerUuids = getCustomerUuids(vendorQuoteEntities);
        // 取得廠商清單
        List<VendorEntity> vendorEntities = vendorService.findAllVendorUuidIn(vendorUuids);
        // 取得客戶清單
        List<CustomerEntity> customerEntities = customerService.findAllCustomerUuidIn(customerUuids);
        VendorEntity vendorEntity;
        CustomerEntity customerEntity;
        for(VendorQuoteEntity vendorQuoteEntity : vendorQuoteEntities){
            vendorEntity = CommonUtil.getEntityByUuid(vendorEntities, vendorQuoteEntity.getVendorUuid());
            if(null == vendorEntity){
                continue;
            }
            customerEntity = CommonUtil.getEntityByUuid(customerEntities, vendorQuoteEntity.getCustomerUuid());
            if(null == customerEntity){
                continue;
            }
            responses.add(new VendorQuoteFindAllResponse(
                    vendorQuoteEntity.getQuoteUuid(),
                    vendorEntity.getName(),
                    customerEntity.getName(),
                    vendorQuoteEntity.getVendorQuoteStatus().getValue(),
                    InstantUtil.to(vendorQuoteEntity.getCreateTime())
            ));
        }
        return responses;
    }

    // 取得廠商uuid清單
    private List<UUID> getVendorUuids(List<VendorQuoteEntity> vendorQuoteEntities){
        List<UUID> vendorUuids = new ArrayList<>();
        if(null == vendorQuoteEntities || vendorQuoteEntities.isEmpty()){
            return vendorUuids;
        }
        for(VendorQuoteEntity vendorQuoteEntity : vendorQuoteEntities){
            vendorUuids.add(vendorQuoteEntity.getVendorUuid());
        }
        return vendorUuids;
    }

    // 取得客戶uuid清單
    private List<UUID> getCustomerUuids(List<VendorQuoteEntity> vendorQuoteEntities){
        List<UUID> customerUuids = new ArrayList<>();
        if(null == vendorQuoteEntities || vendorQuoteEntities.isEmpty()){
            return customerUuids;
        }
        for(VendorQuoteEntity vendorQuoteEntity : vendorQuoteEntities){
            customerUuids.add(vendorQuoteEntity.getCustomerUuid());
        }
        return customerUuids;
    }

}
