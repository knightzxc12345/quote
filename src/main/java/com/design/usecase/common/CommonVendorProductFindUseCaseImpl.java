package com.design.usecase.common;

import com.design.controller.common.response.CommonVendorProductFindAllResponse;
import com.design.entity.vendor_product.VendorProductEntity;
import com.design.service.vendor_product.VendorProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonVendorProductFindUseCaseImpl implements CommonVendorProductFindUseCase {

    private final VendorProductService vendorProductService;

    @Override
    public List<CommonVendorProductFindAllResponse> findAll() {
        List<VendorProductEntity> vendorProductEntities = vendorProductService.findAll();
        return format(vendorProductEntities);
    }

    private List<CommonVendorProductFindAllResponse> format(List<VendorProductEntity> vendorProductEntities){
        List<CommonVendorProductFindAllResponse> responses = new ArrayList<>();
        if(null == vendorProductEntities || vendorProductEntities.isEmpty()){
            return responses;
        }
        for(VendorProductEntity vendorProductEntity : vendorProductEntities){
            responses.add(new CommonVendorProductFindAllResponse(
                    vendorProductEntity.getVendorUuid(),
                    vendorProductEntity.getUuid(),
                    vendorProductEntity.getName(),
                    vendorProductEntity.getUnitPrice()
            ));
        }
        return responses;
    }

}
