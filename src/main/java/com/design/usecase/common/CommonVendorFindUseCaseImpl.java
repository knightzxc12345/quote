package com.design.usecase.common;

import com.design.controller.common.response.CommonVendorFindAllResponse;
import com.design.entity.vendor.VendorEntity;
import com.design.service.vendor.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonVendorFindUseCaseImpl implements CommonVendorFindUseCase {

    private final VendorService vendorService;

    @Override
    public List<CommonVendorFindAllResponse> findAll() {
        List<VendorEntity> vendorEntities = vendorService.findAll();
        return format(vendorEntities);
    }

    private List<CommonVendorFindAllResponse> format(List<VendorEntity> vendorEntities){
        List<CommonVendorFindAllResponse> responses = new ArrayList<>();
        if(null == vendorEntities || vendorEntities.isEmpty()){
            return responses;
        }
        for(VendorEntity vendorEntity : vendorEntities){
            responses.add(new CommonVendorFindAllResponse(
                    vendorEntity.getUuid(),
                    vendorEntity.getName()
            ));
        }
        return responses;
    }

}
