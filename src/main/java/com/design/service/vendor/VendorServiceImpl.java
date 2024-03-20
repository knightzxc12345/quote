package com.design.service.vendor;

import com.design.base.eunms.VendorEnum;
import com.design.entity.vendor.VendorEntity;
import com.design.handler.BusinessException;
import com.design.repository.vendor.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    @Override
    public VendorEntity create(VendorEntity vendorEntity, String userUuid) {
        VendorEntity isExists = vendorRepository.findByIsDeletedFalseAndName(vendorEntity.getName());
        if(null != isExists){
            throw new BusinessException(VendorEnum.VE0001);
        }
        vendorEntity.setIsDeleted(false);
        vendorEntity.setUuid(UUID.randomUUID().toString());
        vendorEntity.setCreateTime(Instant.now());
        vendorEntity.setCreateUser(userUuid);
        return vendorRepository.save(vendorEntity);
    }

    @Override
    public void update(VendorEntity vendorEntity, String userUuid) {
        VendorEntity isExists = vendorRepository.findByIsDeletedFalseAndName(vendorEntity.getName());
        if(null != isExists && !vendorEntity.getUuid().equals(isExists.getUuid())){
            throw new BusinessException(VendorEnum.VE0001);
        }
        vendorEntity.setModifiedTime(Instant.now());
        vendorEntity.setModifiedUser(userUuid);
        vendorRepository.save(vendorEntity);
    }

    @Override
    public void delete(VendorEntity vendorEntity, String userUuid) {
        vendorEntity.setIsDeleted(true);
        vendorEntity.setDeletedTime(Instant.now());
        vendorEntity.setDeletedUser(userUuid);
        vendorRepository.save(vendorEntity);
    }

    @Override
    public VendorEntity findByUuid(String vendorUuid) {
        return vendorRepository.findByIsDeletedFalseAndUuid(vendorUuid);
    }

    @Override
    public List<VendorEntity> findAll() {
        return vendorRepository.findByIsDeletedFalseOrderByNameAsc();
    }

    @Override
    public List<VendorEntity> findAllLike(String keyword) {
        return vendorRepository.findAll(keyword);
    }

    @Override
    public Page<VendorEntity> findAllLikeByPage(String keyword, Pageable pageable) {
        return vendorRepository.findAllByPage(keyword, pageable);
    }

}
