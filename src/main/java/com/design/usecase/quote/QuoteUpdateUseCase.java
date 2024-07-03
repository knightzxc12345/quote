package com.design.usecase.quote;

import com.design.controller.quote.request.QuoteUpdateRequest;

import java.util.UUID;

public interface QuoteUpdateUseCase {

    void update(QuoteUpdateRequest request, UUID quoteUuid);

}
