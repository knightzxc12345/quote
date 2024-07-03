package com.design.usecase.vendor;

import com.design.controller.vendor.request.VendorUpdateRequest;
import com.design.entity.vendor.VendorEntity;
import com.design.service.vendor.VendorService;
import com.design.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorUpdateUseCaseImpl implements VendorUpdateUseCase {

    private final VendorService vendorService;

    @Override
    public void update(VendorUpdateRequest request, UUID vendorUuid) {
        VendorEntity vendorEntity = vendorService.findByUuid(vendorUuid);
        vendorEntity = update(vendorEntity, request);
        // 更新廠商
        vendorService.update(vendorEntity, JwtUtil.extractUserUuid());
    }

    private VendorEntity update(VendorEntity vendorEntity, VendorUpdateRequest request){
        vendorEntity.setName(request.name());
        vendorEntity.setAddress(request.address());
        vendorEntity.setMobile(request.mobile());
        vendorEntity.setTel(request.tel());
        vendorEntity.setFax(request.fax());
        return vendorEntity;
    }

}
