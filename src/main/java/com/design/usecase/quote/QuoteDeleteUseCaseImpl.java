package com.design.usecase.quote;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class QuoteDeleteUseCaseImpl implements QuoteDeleteUseCase {

    @Override
    public void delete(UUID quoteUuid) {

    }

}
