package com.quote.controller.customer.request;

import jakarta.validation.constraints.Min;

public record CustomerFindRequest(

        @Min(value = 0, message = "頁數不得小於0")
        Integer page,

        @Min(value = 0, message = "每頁筆數不得小於0")
        Integer size,

        String keyword

) {
}
