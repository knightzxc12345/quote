package com.design.controller.item.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ItemCreateRequest(

        @NotBlank(message = "編號不得為空")
        String no,

        @NotBlank(message = "名稱不得為空")
        String name,

        @NotBlank(message = "規格不得為空")
        String spec,

        @NotBlank(message = "單位不得為空")
        String unit,

        @Min(0)
        @NotNull(message = "總計")
        BigDecimal amount,

        @NotNull(message = "廠商產品清單不得為空")
        List<ItemCreateRequest.VendorProduct> vendorProducts

) {

        public record VendorProduct(

                @NotNull(message = "廠商產品不得為空")
                UUID vendorProductUuid,

                @NotNull(message = "數量不得為空")
                Integer qty

        ){
        }

}
