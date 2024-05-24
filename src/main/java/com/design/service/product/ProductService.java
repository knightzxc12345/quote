package com.design.service.product;

import com.design.entity.product.ProductEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    ProductEntity create(ProductEntity productEntity, String userUuid);

    void update(ProductEntity productEntity, String userUuid);

    void delete(ProductEntity productEntity, String userUuid);

    void deleteAll(List<ProductEntity> productEntities, String userUuid);

    ProductEntity findByUuid(String productUuid);

    List<ProductEntity> findAll();

    List<ProductEntity> findAllByProductUuidIn(List<String> productUuids);

    List<ProductEntity> findAllByItemUuid(String itemUuid);

    List<ProductEntity> findAllLike(
            String keyword
    );

    Page<ProductEntity> findAllLikeByPage(
            String keyword,
            Integer page,
            Integer size
    );

}
