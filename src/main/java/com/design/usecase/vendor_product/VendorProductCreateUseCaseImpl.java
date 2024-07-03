package com.design.usecase.vendor_product;

import com.design.controller.vendor_product.request.VendorProductCreateRequest;
import com.design.entity.vendor_product.VendorProductEntity;
import com.design.service.vendor_product.VendorProductService;
import com.design.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class VendorProductCreateUseCaseImpl implements VendorProductCreateUseCase {

    private final VendorProductService vendorProductService;

    @Override
    public void create(VendorProductCreateRequest request) {
        VendorProductEntity vendorProductEntity = init(request);
        vendorProductService.create(vendorProductEntity, JwtUtil.extractUserUuid());
    }

    private VendorProductEntity init(VendorProductCreateRequest request){
        VendorProductEntity vendorProductEntity = new VendorProductEntity();
        vendorProductEntity.setVendorUuid(request.vendorUuid());
        vendorProductEntity.setName(request.name());
        vendorProductEntity.setUnitPrice(new BigDecimal(request.unitPrice()));
        return vendorProductEntity;
    }

}
