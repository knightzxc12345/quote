package com.design.usecase.item;

import com.design.entity.item.ItemEntity;
import com.design.entity.product.ProductEntity;
import com.design.service.item.ItemService;
import com.design.service.product.ProductService;
import com.design.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemDeleteUseCaseImpl implements ItemDeleteUseCase {

    private final ItemService itemService;

    private final ProductService productService;

    @Override
    public void delete(UUID itemUuid) {
        ItemEntity itemEntity = itemService.findByUuid(itemUuid);
        List<ProductEntity> productEntities = productService.findAllByItemUuid(itemUuid);
        // 刪除項目
        itemService.delete(itemEntity, JwtUtil.extractUserUuid());
        // 刪除產品
        productService.deleteAll(productEntities, JwtUtil.extractUserUuid());
    }

}
