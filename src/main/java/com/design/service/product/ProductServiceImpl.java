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

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductEntity create(ProductEntity productEntity, String userUuid) {
        ProductEntity isExists = productRepository.findByItemUuidAndSpecification(
                productEntity.getItemUuid(),
                productEntity.getSpecification()
        );
        if(null != isExists){
            throw new BusinessException(ProductEnum.PR0001);
        }
        productEntity.setIsDeleted(false);
        productEntity.setCreateTime(Instant.now());
        productEntity.setCreateUser(userUuid);
        return productRepository.save(productEntity);
    }

    @Override
    public void update(ProductEntity productEntity, String userUuid) {
        ProductEntity isExists = productRepository.findByItemUuidAndSpecification(
                productEntity.getItemUuid(),
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
    public void deleteAll(List<ProductEntity> productEntities, String userUuid) {
        if(null == productEntities || productEntities.isEmpty()){
            return;
        }
        for(ProductEntity productEntity : productEntities){
            productEntity.setIsDeleted(true);
            productEntity.setDeletedTime(Instant.now());
            productEntity.setDeletedUser(userUuid);
        }
        productRepository.saveAll(productEntities);
    }

    @Override
    public ProductEntity findByUuid(String productUuid) {
        return productRepository.findByIsDeletedFalseAndUuid(productUuid);
    }

    @Override
    public List<ProductEntity> findAll() {
        return productRepository.findByIsDeletedFalseOrderByItemUuidAscSpecificationAsc();
    }

    @Override
    public List<ProductEntity> findAllByItemUuid(String itemUuid) {
        return productRepository.findByIsDeletedFalseAndItemUuid(itemUuid);
    }

    @Override
    public List<ProductEntity> findAllLike(
            String keyword) {
        return productRepository.findAll(
                keyword
        );
    }

    @Override
    public Page<ProductEntity> findAllLikeByPage(
            String keyword,
            Pageable pageable) {
        return productRepository.findAllByPage(
                keyword,
                pageable
        );
    }

}
