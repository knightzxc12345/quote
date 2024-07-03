package com.design.service.product;

import com.design.entity.product.ProductEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductEntity create(ProductEntity productEntity, UUID userUuid);

    void update(ProductEntity productEntity, UUID userUuid);

    void delete(ProductEntity productEntity, UUID userUuid);

    void deleteAll(List<ProductEntity> productEntities, UUID userUuid);

    ProductEntity findByUuid(UUID productUuid);

    List<ProductEntity> findAll();

    List<ProductEntity> findAllByProductUuidIn(List<UUID> productUuids);

    List<ProductEntity> findAllByItemUuid(UUID itemUuid);

    List<ProductEntity> findAllLike(
            String keyword
    );

    Page<ProductEntity> findAllLikeByPage(
            String keyword,
            Integer page,
            Integer size
    );

}
