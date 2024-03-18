package com.quote.usecase.vendor;

import com.quote.controller.vendor.request.VendorFindRequest;
import com.quote.controller.vendor.response.VendorFindAllResponse;
import com.quote.controller.vendor.response.VendorFindPageResponse;
import com.quote.controller.vendor.response.VendorFindResponse;
import com.quote.entity.vendor.VendorEntity;
import com.quote.service.vendor.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorFindUseCaseImpl implements VendorFindUseCase {

    private final VendorService vendorService;

    @Override
    public VendorFindResponse findByUuid(String vendorUuid) {
        VendorEntity vendorEntity = vendorService.findByUuid(vendorUuid);
        return format(vendorEntity);
    }

    @Override
    public List<VendorFindAllResponse> findAll(VendorFindRequest request) {
        List<VendorEntity> vendorEntities = vendorService.findAllLike(request.keyword());
        return format(vendorEntities);
    }

    @Override
    public VendorFindPageResponse findAllByPage(VendorFindRequest request) {
        Integer page = request.page();
        Integer size = null == request.size() ? 10 : request.size();
        Pageable pageable = PageRequest.of(page, size);
        Page<VendorEntity> vendorEntityPage = vendorService.findAllLikeByPage(request.keyword(), pageable);
        List<VendorFindAllResponse> responses = format(vendorEntityPage.getContent());
        return new VendorFindPageResponse(
                vendorEntityPage.getTotalPages(),
                vendorEntityPage.getNumber(),
                vendorEntityPage.getSize(),
                responses
        );
    }

    private VendorFindResponse format(VendorEntity vendorEntity){
        if(null == vendorEntity){
            return null;
        }
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
