package com.quote.service.quote;

import com.quote.entity.quote.QuoteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuoteService {

    QuoteEntity create(QuoteEntity quoteEntity, String userUuid);

    void update(QuoteEntity quoteEntity, String userUuid);

    void delete(QuoteEntity quoteEntity, String userUuid);

    QuoteEntity findByUuid(String quoteUuid);

    List<QuoteEntity> findAllLike(
            String userUuid,
            String keyword
    );

    Page<QuoteEntity> findAllLikeByPage(
            String userUuid,
            String keyword,
            Pageable pageable
    );

}
