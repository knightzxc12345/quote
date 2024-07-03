package com.design.usecase.vendor;

import com.design.controller.vendor.request.VendorFindRequest;
import com.design.controller.vendor.response.VendorFindAllResponse;
import com.design.controller.vendor.response.VendorFindPageResponse;
import com.design.controller.vendor.response.VendorFindResponse;
import com.design.entity.vendor.VendorEntity;
import com.design.service.vendor.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorFindUseCaseImpl implements VendorFindUseCase {

    private final VendorService vendorService;

    @Override
    public VendorFindResponse findByUuid(UUID vendorUuid) {
        VendorEntity vendorEntity = vendorService.findByUuid(vendorUuid);
        return format(vendorEntity);
    }

    @Override
    public List<VendorFindAllResponse> findAll(VendorFindRequest request) {
        List<VendorEntity> vendorEntities = vendorService.findAllLike(
                request.keyword()
        );
        return format(vendorEntities);
    }

    @Override
    public VendorFindPageResponse findAllByPage(VendorFindRequest request) {
        Page<VendorEntity> vendorEntityPage = vendorService.findAllLikeByPage(
                request.keyword(),
                request.page(),
                request.size()
        );
        List<VendorFindAllResponse> responses = format(vendorEntityPage.getContent());
        return new VendorFindPageResponse(
                vendorEntityPage.getTotalPages(),
                vendorEntityPage.getNumber(),
                vendorEntityPage.getSize(),
                responses
        );
    }

    private VendorFindResponse format(VendorEntity vendorEntity){
        return new VendorFindResponse(
                vendorEntity.getUuid(),
                vendorEntity.getName(),
                vendorEntity.getAddress(),
                vendorEntity.getMobile(),
                vendorEntity.getTel(),
                vendorEntity.getFax()
        );
    }

    private List<VendorFindAllResponse> format(List<VendorEntity> vendorEntities){
        List<VendorFindAllResponse> responses = new ArrayList<>();
        if(null == vendorEntities || vendorEntities.isEmpty()){
            return responses;
        }
        for(VendorEntity vendorEntity : vendorEntities){
            responses.add(new VendorFindAllResponse(
                    vendorEntity.getUuid(),
                    vendorEntity.getName(),
                    vendorEntity.getAddress(),
                    vendorEntity.getMobile(),
                    vendorEntity.getTel(),
                    vendorEntity.getFax()
            ));
        }
        return responses;
    }

}
