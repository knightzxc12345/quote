package com.design.service.product;

import com.design.entity.product.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    ProductEntity create(ProductEntity productEntity, String userUuid);

    void update(ProductEntity productEntity, String userUuid);

    void delete(ProductEntity productEntity, String userUuid);

    void deleteAll(List<ProductEntity> productEntities, String userUuid);

    ProductEntity findByUuid(String productUuid);

    List<ProductEntity> findAll();

    List<ProductEntity> findAllByItemUuid(String itemUuid);

    List<ProductEntity> findAllByItemUuids(List<String> itemUuid);

    List<ProductEntity> findAllLike(
            String keyword
    );

    Page<ProductEntity> findAllLikeByPage(
            String keyword,
            Pageable pageable
    );

}
