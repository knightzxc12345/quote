package com.design.usecase.quote;

import com.design.controller.quote.request.QuoteUpdateRequest;

public interface QuoteUpdateUseCase {

    void update(QuoteUpdateRequest request, String quoteUuid);

}
