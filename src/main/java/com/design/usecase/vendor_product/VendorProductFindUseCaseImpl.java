package com.design.usecase.vendor_product;

import com.design.controller.vendor_product.request.VendorProductFindRequest;
import com.design.controller.vendor_product.response.VendorProductFindAllResponse;
import com.design.controller.vendor_product.response.VendorProductFindPageResponse;
import com.design.controller.vendor_product.response.VendorProductFindResponse;
import com.design.entity.vendor_product.VendorProductEntity;
import com.design.service.vendor_product.VendorProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorProductFindUseCaseImpl implements VendorProductFindUseCase {

    private final VendorProductService vendorProductService;

    @Override
    public VendorProductFindResponse findByUuid(UUID vendorProductUuid) {
        VendorProductEntity vendorProductEntity = vendorProductService.findByUuid(vendorProductUuid);
        return new VendorProductFindResponse(
                vendorProductEntity.getVendorUuid(),
                vendorProductEntity.getName(),
                vendorProductEntity.getUnitPrice().longValue()
        );
    }

    @Override
    public List<VendorProductFindAllResponse> findAll(VendorProductFindRequest request) {
        String vendorUuid = null == request.vendorUuid() ? null : request.vendorUuid().toString();
        List<VendorProductEntity> vendorProductEntities = vendorProductService.findAllLike(
                vendorUuid,
                request.keyword()
        );
        return format(vendorProductEntities);
    }

    @Override
    public VendorProductFindPageResponse findAllByPage(VendorProductFindRequest request) {
        String vendorUuid = null == request.vendorUuid() ? null : request.vendorUuid().toString();
        Page<VendorProductEntity> vendorProductEntityPage = vendorProductService.findAllLikeByPage(
                vendorUuid,
                request.keyword(),
                request.page(),
                request.size()
        );
        List<VendorProductFindAllResponse> responses = format(vendorProductEntityPage.getContent());
        return new VendorProductFindPageResponse(
                vendorProductEntityPage.getTotalPages(),
                vendorProductEntityPage.getNumber(),
                vendorProductEntityPage.getSize(),
                responses
        );
    }

    private List<VendorProductFindAllResponse> format(List<VendorProductEntity> vendorProductEntities){
        List<VendorProductFindAllResponse> responses = new ArrayList<>();
        if(null == vendorProductEntities || vendorProductEntities.isEmpty()){
            return responses;
        }
        for(VendorProductEntity vendorProductEntity : vendorProductEntities){
            responses.add(new VendorProductFindAllResponse(
                    vendorProductEntity.getUuid(),
                    vendorProductEntity.getVendorUuid(),
                    vendorProductEntity.getName(),
                    vendorProductEntity.getUnitPrice().longValue()
            ));
        }
        return responses;
    }

}
