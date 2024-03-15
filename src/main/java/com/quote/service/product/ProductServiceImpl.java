package com.quote.service.product;

import com.quote.entity.product.ProductEntity;
import com.quote.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public void create(ProductEntity productEntity, String userUuid) {
        productEntity.setUuid(UUID.randomUUID().toString());
        productEntity.setIsDeleted(false);
        productEntity.setCreateTime(Instant.now());
        productEntity.setCreateUser(userUuid);
        productRepository.save(productEntity);
    }

    @Override
    public void update(ProductEntity productEntity, String userUuid) {
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

}
