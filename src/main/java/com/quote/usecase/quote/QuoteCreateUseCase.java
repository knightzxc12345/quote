package com.quote.usecase.quote;

import com.quote.controller.quote.request.QuoteCreateRequest;

public interface QuoteCreateUseCase {

    void create(QuoteCreateRequest request);

}
