package com.design.controller.item.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record ItemUpdateRequest(

        @NotBlank(message = "編號不得為空")
        String no,

        @NotBlank(message = "名稱不得為空")
        String name,

        @NotBlank(message = "規格不得為空")
        String spec,

        @NotNull(message = "廠商產品uuid清單不得為空")
        List<ItemUpdateRequest.VendorProduct> vendorProducts

) {

        public record VendorProduct(

                @NotNull(message = "廠商產品不得為空")
                UUID vendorProductUuid,

                @NotNull(message = "數量不得為空")
                Integer qty

        ){
        }

}
