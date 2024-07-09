package com.design.usecase.quote;

import com.design.controller.quote.request.QuoteCreateRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class QuoteCreateUseCaseImpl implements QuoteCreateUseCase {

    @Override
    public void create(QuoteCreateRequest request) {

    }

}
