package com.quote.usecase.vendor;

import com.quote.controller.vendor.request.VendorCreateRequest;
import com.quote.entity.vendor.VendorEntity;
import com.quote.service.vendor.VendorService;
import com.quote.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendorCreateUseCaseImpl implements VendorCreateUseCase {

    private final VendorService vendorService;

    @Override
    public void create(VendorCreateRequest request) {
        VendorEntity vendorEntity = new VendorEntity();
        vendorEntity.setName(request.name());
        vendorEntity.setAddress(request.address());
        vendorEntity.setMobile(request.mobile());
        vendorEntity.setTel(request.tel());
        vendorEntity.setFax(request.fax());
        vendorService.create(vendorEntity, JwtUtil.extractUsername());
    }

}
