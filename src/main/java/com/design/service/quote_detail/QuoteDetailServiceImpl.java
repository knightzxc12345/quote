package com.design.service.quote_detail;

import com.design.entity.quote_detail.QuoteDetailEntity;
import com.design.repository.quote_detail.QuoteDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuoteDetailServiceImpl implements QuoteDetailService {

    private final QuoteDetailRepository quoteDetailRepository;

    @Override
    public void createAll(List<QuoteDetailEntity> quoteDetailEntities, UUID userUuid) {
        if(null == quoteDetailEntities || quoteDetailEntities.isEmpty()){
            return;
        }
        for(QuoteDetailEntity quoteDetailEntity : quoteDetailEntities){
            quoteDetailEntity.setIsDeleted(false);
            quoteDetailEntity.setUuid(UUID.randomUUID());
            quoteDetailEntity.setCreateTime(Instant.now());
            quoteDetailEntity.setCreateUser(userUuid);
        }
        quoteDetailRepository.saveAll(quoteDetailEntities);
    }

    @Override
    public void updateAll(List<QuoteDetailEntity> quoteDetailEntities, UUID userUuid) {
        if(null == quoteDetailEntities || quoteDetailEntities.isEmpty()){
            return;
        }
        for(QuoteDetailEntity quoteDetailEntity : quoteDetailEntities){
            quoteDetailEntity.setModifiedTime(Instant.now());
            quoteDetailEntity.setModifiedUser(userUuid);
        }
        quoteDetailRepository.saveAll(quoteDetailEntities);
    }

    @Override
    public void deleteAll(List<QuoteDetailEntity> quoteDetailEntities, UUID userUuid) {
        if(null == quoteDetailEntities || quoteDetailEntities.isEmpty()){
            return;
        }
        for(QuoteDetailEntity quoteDetailEntity : quoteDetailEntities){
            quoteDetailEntity.setIsDeleted(true);
            quoteDetailEntity.setDeletedTime(Instant.now());
            quoteDetailEntity.setDeletedUser(userUuid);
        }
        quoteDetailRepository.saveAll(quoteDetailEntities);
    }

    @Override
    public List<QuoteDetailEntity> findAll(UUID quoteUuid) {
        return quoteDetailRepository.findByIsDeletedFalseAndQuoteUuidOrderByPkAsc(quoteUuid);
    }

}
