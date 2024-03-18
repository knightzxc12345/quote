package com.quote.usecase.vendor;

import com.quote.controller.vendor.request.VendorUpdateRequest;
import com.quote.entity.vendor.VendorEntity;
import com.quote.service.vendor.VendorService;
import com.quote.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendorUpdateUseCaseImpl implements VendorUpdateUseCase {

    private final VendorService vendorService;

    @Override
    public void update(VendorUpdateRequest request, String vendorUuid) {
        VendorEntity vendorEntity = vendorService.findByUuid(vendorUuid);
        vendorEntity.setName(request.name());
        vendorEntity.setAddress(request.address());
        vendorEntity.setMobile(request.mobile());
        vendorEntity.setTel(request.tel());
        vendorEntity.setFax(request.fax());
        vendorService.update(vendorEntity, JwtUtil.extractUsername());
    }

}
