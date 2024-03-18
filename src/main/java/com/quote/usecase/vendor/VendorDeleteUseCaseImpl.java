package com.quote.usecase.vendor;

import com.quote.entity.vendor.VendorEntity;
import com.quote.service.vendor.VendorService;
import com.quote.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendorDeleteUseCaseImpl implements VendorDeleteUseCase {

    private final VendorService vendorService;

    @Override
    public void delete(String vendorUuid) {
        VendorEntity vendorEntity = vendorService.findByUuid(vendorUuid);
        vendorService.delete(vendorEntity, JwtUtil.extractUsername());
    }

}
