package com.design.service.product;

import com.design.base.eunms.ProductEnum;
import com.design.entity.product.ProductEntity;
import com.design.handler.BusinessException;
import com.design.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public void create(ProductEntity productEntity, String userUuid) {
        ProductEntity isExists = productRepository.findByVendorUuidAndNameAndSpecification(
                productEntity.getVendorUuid(),
                productEntity.getName(),
                productEntity.getSpecification()
        );
        if(null != isExists){
            throw new BusinessException(ProductEnum.PR0001);
        }
        productEntity.setUuid(UUID.randomUUID().toString());
        productEntity.setIsDeleted(false);
        productEntity.setCreateTime(Instant.now());
        productEntity.setCreateUser(userUuid);
        productRepository.save(productEntity);
    }

    @Override
    public void update(ProductEntity productEntity, String userUuid) {
        ProductEntity isExists = productRepository.findByVendorUuidAndNameAndSpecification(
                productEntity.getVendorUuid(),
                productEntity.getName(),
                productEntity.getSpecification()
        );
        if(null != isExists && !productEntity.getUuid().equals(isExists.getUuid())){
            throw new BusinessException(ProductEnum.PR0001);
        }
        productEntity.setModifiedTime(Instant.now());
        productEntity.setModifiedUser(userUuid);
        productRepository.save(productEntity);
    }

    @Override
    public void delete(ProductEntity productEntity, String userUuid) {
        productEntity.setIsDeleted(true);
        productEntity.setDeletedTime(Instant.now());
        productEntity.setDeletedUser(userUuid);
        productRepository.save(productEntity);
    }

    @Override
    public ProductEntity findByUuid(String productUuid) {
        return productRepository.findByIsDeletedFalseAndUuid(productUuid);
    }

    @Override
    public List<ProductEntity> findAll() {
        return productRepository.findByIsDeletedFalseOrderByNoAscNameAscSpecificationAsc();
    }

    @Override
    public List<ProductEntity> findAllLike(String keyword) {
        return productRepository.findAll(keyword);
    }

    @Override
    public Page<ProductEntity> findAllLikeByPage(String keyword, Pageable pageable) {
        return productRepository.findAllByPage(keyword, pageable);
    }

}
