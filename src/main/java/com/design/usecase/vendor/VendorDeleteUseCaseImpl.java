package com.design.usecase.vendor;

import com.design.entity.vendor.VendorEntity;
import com.design.service.vendor.VendorService;
import com.design.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorDeleteUseCaseImpl implements VendorDeleteUseCase {

    private final VendorService vendorService;

    @Override
    public void delete(UUID vendorUuid) {
        VendorEntity vendorEntity = vendorService.findByUuid(vendorUuid);
        vendorService.delete(vendorEntity, JwtUtil.extractUserUuid());
    }

}
