package com.design.service.quote;

import com.design.entity.quote.QuoteEntity;
import com.design.repository.quote.QuoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuoteServiceImpl implements QuoteService {

    private final QuoteRepository quoteRepository;

    @Override
    public QuoteEntity create(QuoteEntity quoteEntity, String userUuid) {
        quoteEntity.setIsDeleted(false);
        quoteEntity.setUuid(UUID.randomUUID().toString());
        quoteEntity.setCreateTime(Instant.now());
        quoteEntity.setCreateUser(userUuid);
        return quoteRepository.save(quoteEntity);
    }

    @Override
    public void update(QuoteEntity quoteEntity, String userUuid) {
        quoteEntity.setModifiedTime(Instant.now());
        quoteEntity.setModifiedUser(userUuid);
        quoteRepository.save(quoteEntity);
    }

    @Override
    public void delete(QuoteEntity quoteEntity, String userUuid) {
        quoteEntity.setIsDeleted(true);
        quoteEntity.setModifiedTime(Instant.now());
        quoteEntity.setModifiedUser(userUuid);
        quoteRepository.save(quoteEntity);
    }

    @Override
    public QuoteEntity findByUuid(String quoteUuid) {
        return quoteRepository.findByIsDeletedFalseAndUuid(quoteUuid);
    }

    @Override
    public List<QuoteEntity> findAllLike(
            String userUuid,
            String keyword) {
        return quoteRepository.findAll(
                userUuid,
                keyword
        );
    }

    @Override
    public Page<QuoteEntity> findAllLikeByPage(
            String userUuid,
            String keyword,
            Pageable pageable) {
        return quoteRepository.findAllByPage(
                userUuid,
                keyword,
                pageable
        );
    }

}
