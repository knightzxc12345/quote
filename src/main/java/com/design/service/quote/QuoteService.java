package com.design.service.quote;

import com.design.entity.quote.QuoteEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface QuoteService {

    QuoteEntity create(QuoteEntity quoteEntity, UUID userUuid);

    void update(QuoteEntity quoteEntity, UUID userUuid);

    void delete(QuoteEntity quoteEntity, UUID userUuid);

    QuoteEntity findByUuid(UUID quoteUuid);

    List<QuoteEntity> findAllLike(
            String userUuid,
            String customerUuid,
            String keyword
    );

    Page<QuoteEntity> findAllLikeByPage(
            String userUuid,
            String customerUuid,
            String keyword,
            Integer page,
            Integer size
    );

}
