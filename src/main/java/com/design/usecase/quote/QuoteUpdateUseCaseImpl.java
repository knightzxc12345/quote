package com.design.usecase.quote;

import com.design.controller.quote.request.QuoteUpdateRequest;
import com.design.service.quote.QuoteService;
import com.design.service.quote_detail.QuoteDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuoteUpdateUseCaseImpl implements QuoteUpdateUseCase {

    private final QuoteService quoteService;

    private final QuoteDetailService quoteDetailService;

    @Override
    public void update(QuoteUpdateRequest request, String quoteUuid) {

    }

}
