package com.quote.service.product;

import com.quote.entity.product.ProductEntity;

public interface ProductService {

    void create(ProductEntity productEntity, String userUuid);

    void update(ProductEntity productEntity, String userUuid);

    void delete(ProductEntity productEntity, String userUuid);

    ProductEntity findByUuid(String productUuid);

}
