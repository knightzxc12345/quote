package com.design.usecase.quote;

import com.design.controller.quote.request.QuoteFindRequest;
import com.design.controller.quote.response.QuoteFindAllResponse;
import com.design.controller.quote.response.QuoteFindPageResponse;
import com.design.controller.quote.response.QuoteFindResponse;
import com.design.entity.quote.QuoteEntity;
import com.design.entity.quote_detail.QuoteDetailEntity;
import com.design.service.quote.QuoteService;
import com.design.service.quote_detail.QuoteDetailService;
import com.design.utils.InstantUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuoteFindUseCaseImpl implements QuoteFindUseCase {

    private final QuoteService quoteService;

    private final QuoteDetailService quoteDetailService;

    @Override
    public QuoteFindResponse findByUuid(UUID quoteUuid) {
        QuoteEntity quoteEntity = quoteService.findByUuid(quoteUuid);
        List<QuoteDetailEntity> quoteDetailEntities = quoteDetailService.findAll(quoteUuid);
        List<QuoteFindResponse.Product> products = getProducts(quoteDetailEntities);
        return new QuoteFindResponse(
                quoteEntity.getUuid(),
                quoteEntity.getUserUuid(),
                quoteEntity.getCustomerUuid(),
                quoteEntity.getUnderTakerName(),
                quoteEntity.getUnderTakerTel(),
                products
        );
    }

    @Override
    public List<QuoteFindAllResponse> findAll(QuoteFindRequest request) {
        String userUuid = null == request.userUuid() ? null : request.userUuid().toString();
        String customerUuid = null == request.customerUuid() ? null : request.customerUuid().toString();
        List<QuoteEntity> quoteEntities = quoteService.findAllLike(
                userUuid,
                customerUuid,
                request.keyword()
        );
        return format(quoteEntities);
    }

    @Override
    public QuoteFindPageResponse findAllByPage(QuoteFindRequest request) {
        String userUuid = null == request.userUuid() ? null : request.userUuid().toString();
        String customerUuid = null == request.customerUuid() ? null : request.customerUuid().toString();
        Page<QuoteEntity> quoteEntityPage = quoteService.findAllLikeByPage(
                userUuid,
                customerUuid,
                request.keyword(),
                request.page(),
                request.size()
        );
        List<QuoteFindAllResponse> responses = format(quoteEntityPage.getContent());
        return new QuoteFindPageResponse(
                quoteEntityPage.getTotalPages(),
                quoteEntityPage.getNumber(),
                quoteEntityPage.getSize(),
                responses
        );
    }

    private List<QuoteFindResponse.Product> getProducts(List<QuoteDetailEntity> quoteDetailEntities){
        List<QuoteFindResponse.Product> products = new ArrayList<>();
        if(null == quoteDetailEntities || quoteDetailEntities.isEmpty()){
            return products;
        }
        for(QuoteDetailEntity quoteDetailEntity : quoteDetailEntities){
            products.add(new QuoteFindResponse.Product(
                    quoteDetailEntity.getItemUuid(),
                    null,
                    null,
                    null
            ));
        }
        return products;
    }

    private List<QuoteFindAllResponse> format(List<QuoteEntity> quoteEntities){
        List<QuoteFindAllResponse> responses = new ArrayList<>();
        if(null == quoteEntities || quoteEntities.isEmpty()){
            return responses;
        }
        for(QuoteEntity quoteEntity : quoteEntities){
            responses.add(new QuoteFindAllResponse(
                    quoteEntity.getUuid(),
                    quoteEntity.getUserUuid(),
                    quoteEntity.getCustomerUuid(),
                    quoteEntity.getTotalAmount(),
                    quoteEntity.getCustomTotalAmount(),
                    quoteEntity.getCostTotalAmount(),
                    InstantUtil.to(quoteEntity.getCreateTime()),
                    quoteEntity.getQuoteStatus().get()
            ));
        }
        return responses;
    }

}
