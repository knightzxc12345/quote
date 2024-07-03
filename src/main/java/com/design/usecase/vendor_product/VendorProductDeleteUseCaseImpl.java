package com.design.usecase.vendor_product;

import com.design.entity.vendor_product.VendorProductEntity;
import com.design.service.vendor_product.VendorProductService;
import com.design.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorProductDeleteUseCaseImpl implements VendorProductDeleteUseCase {

    private final VendorProductService vendorProductService;

    @Override
    public void delete(UUID vendorProductUuid) {
        VendorProductEntity vendorProductEntity = vendorProductService.findByUuid(vendorProductUuid);
        vendorProductService.delete(vendorProductEntity, JwtUtil.extractUserUuid());
    }

}
