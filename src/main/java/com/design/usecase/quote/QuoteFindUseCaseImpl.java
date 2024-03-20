package com.design.usecase.quote;

import com.design.controller.quote.request.QuoteFindRequest;
import com.design.controller.quote.response.QuoteFindAllResponse;
import com.design.controller.quote.response.QuoteFindPageResponse;
import com.design.controller.quote.response.QuoteFindResponse;
import com.design.service.quote.QuoteService;
import com.design.service.quote_detail.QuoteDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuoteFindUseCaseImpl implements QuoteFindUseCase {

    private final QuoteService quoteService;

    private final QuoteDetailService quoteDetailService;

    @Override
    public QuoteFindResponse findByUuid(String quoteUuid) {
        return null;
    }

    @Override
    public List<QuoteFindAllResponse> findAll(QuoteFindRequest request) {
        return null;
    }

    @Override
    public QuoteFindPageResponse findAllByPage(QuoteFindRequest request) {
        return null;
    }

}
