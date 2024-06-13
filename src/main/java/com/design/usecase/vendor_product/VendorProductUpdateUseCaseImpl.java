package com.design.usecase.vendor_product;

import com.design.controller.vendor_product.request.VendorProductUpdateRequest;
import com.design.entity.vendor_product.VendorProductEntity;
import com.design.service.vendor_product.VendorProductService;
import com.design.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class VendorProductUpdateUseCaseImpl implements VendorProductUpdateUseCase {

    private final VendorProductService vendorProductService;

    @Override
    public void update(VendorProductUpdateRequest request, String vendorProductUuid) {
        VendorProductEntity vendorProductEntity = vendorProductService.findByUuid(vendorProductUuid);
        vendorProductEntity.setVendorUuid(request.vendorUuid());
        vendorProductEntity.setName(request.name());
        vendorProductEntity.setUnitPrice(new BigDecimal(request.unitPrice()));
        vendorProductService.update(vendorProductEntity, JwtUtil.extractUsername());
    }

}
