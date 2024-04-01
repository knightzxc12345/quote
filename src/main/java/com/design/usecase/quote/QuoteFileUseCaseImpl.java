package com.design.usecase.quote;

import com.design.base.Common;
import com.design.base.eunms.AuthEnum;
import com.design.controller.quote.response.QuotePreviewResponse;
import com.design.entity.customer.CustomerEntity;
import com.design.entity.quote.QuoteEntity;
import com.design.entity.quote_detail.QuoteDetailEntity;
import com.design.entity.user.UserEntity;
import com.design.handler.BusinessException;
import com.design.modle.QuoteDetail;
import com.design.service.customer.CustomerService;
import com.design.service.quote.QuoteService;
import com.design.service.quote_detail.QuoteDetailService;
import com.design.service.user.UserService;
import com.design.utils.ExcelUtil;
import com.design.utils.HttpUtil;
import com.design.utils.InstantUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuoteFileUseCaseImpl implements QuoteFileUseCase {

    @Value("classpath:files/quote-01.xlsx")
    private Resource quote01Resource;

    private final QuoteService quoteService;

    private final QuoteDetailService quoteDetailService;

    private final CustomerService customerService;

    private final UserService userService;

    @Override
    public QuotePreviewResponse preview(String quoteUuid) {
        QuoteEntity quoteEntity = quoteService.findByUuid(quoteUuid);
        CustomerEntity customerEntity = customerService.findByUuid(quoteEntity.getCustomerUuid());
        UserEntity userEntity = userService.findByUuid(quoteEntity.getUserUuid());
        List<QuoteDetailEntity> quoteDetailEntities = quoteDetailService.findAll(quoteUuid);
        List<QuotePreviewResponse.Product> products = getProducts(quoteDetailEntities);
        return new QuotePreviewResponse(
                userEntity.getName(),
                customerEntity.getName(),
                customerEntity.getAddress(),
                quoteEntity.getUnderTakerName(),
                quoteEntity.getUnderTakerTel(),
                quoteEntity.getAmount(),
                quoteEntity.getTax(),
                quoteEntity.getTotalAmount(),
                quoteEntity.getCustomAmount(),
                quoteEntity.getCustomTax(),
                quoteEntity.getCustomTotalAmount(),
                quoteEntity.getCostAmount(),
                quoteEntity.getCostTax(),
                quoteEntity.getCostTotalAmount(),
                products
        );
    }

    @Override
    public void download(String quoteUuid) {
        QuoteEntity quoteEntity = quoteService.findByUuid(quoteUuid);
        List<QuoteDetailEntity> quoteDetailEntities = quoteDetailService.findAll(quoteUuid);
        // 取得寫入excel陣列資料
        List<QuoteDetail> quoteDetails = getQuoteDetails(quoteDetailEntities);
        // 取得寫入資料
        Map<String, String> params = getParams(quoteEntity);
        // 取得excel原始檔
        InputStream originQuote01InputStream = getInputStream(quote01Resource);
        // 寫入excel
        InputStream quoteInputStream = ExcelUtil.create(originQuote01InputStream, quoteDetails, params);
        writeFile(quoteInputStream);
    }

    private Map<String, String> getParams(QuoteEntity quoteEntity){
        UserEntity userEntity = userService.findByUuid(quoteEntity.getUserUuid());
        Map<String, String> params = new HashMap<>();
        params.put("userName", quoteEntity.getUserName());
        params.put("userMobile", userEntity.getMobile());
        params.put("customerName", quoteEntity.getCustomerName());
        params.put("customerAddress", quoteEntity.getCustomerAddress());
        params.put("underTakerName", quoteEntity.getUnderTakerName());
        params.put("createTime", InstantUtil.to(quoteEntity.getCreateTime(), Common.DATE_FORMAT_2));
        params.put("underTakerTel", quoteEntity.getUnderTakerTel());
        params.put("customAmount", Common.DECIMAL_FORMAT.format(quoteEntity.getCustomAmount()));
        params.put("customTax", Common.DECIMAL_FORMAT.format(quoteEntity.getCustomTax()));
        params.put("customTotalAmount", Common.DECIMAL_FORMAT.format(quoteEntity.getCustomTotalAmount()));
        return params;
    }

    private List<QuotePreviewResponse.Product> getProducts(List<QuoteDetailEntity> quoteDetailEntities){
        List<QuotePreviewResponse.Product> products = new ArrayList<>();
        if(null == quoteDetailEntities || quoteDetailEntities.isEmpty()){
            return products;
        }
        Integer index = 1;
        for(QuoteDetailEntity quoteDetailEntity : quoteDetailEntities){
            products.add(new QuotePreviewResponse.Product(
                    index++,
                    quoteDetailEntity.getItemNo(),
                    quoteDetailEntity.getItemName(),
                    quoteDetailEntity.getProductSpecification(),
                    quoteDetailEntity.getProductUnit(),
                    quoteDetailEntity.getProductQuantity(),
                    quoteDetailEntity.getProductUnitPrice(),
                    quoteDetailEntity.getProductAmount(),
                    quoteDetailEntity.getProductCustomUnitPrice(),
                    quoteDetailEntity.getProductCustomAmount(),
                    quoteDetailEntity.getProductCostPrice(),
                    quoteDetailEntity.getProductCostAmount()
            ));
        }
        return products;
    }

    private List<QuoteDetail> getQuoteDetails(List<QuoteDetailEntity> quoteDetailEntities){
        List<QuoteDetail> quoteDetails = new ArrayList<>();
        if(null == quoteDetailEntities || quoteDetailEntities.isEmpty()){
            return quoteDetails;
        }
        QuoteDetail quoteDetail;
        Integer index = 1;
        for(QuoteDetailEntity quoteDetailEntity : quoteDetailEntities){
            quoteDetail = new QuoteDetail();
            quoteDetail.setIndex(index++);
            quoteDetail.setProductItemNo(quoteDetailEntity.getItemNo());
            quoteDetail.setProductItemName(quoteDetailEntity.getItemName());
            quoteDetail.setProductSpecification(quoteDetailEntity.getProductSpecification());
            quoteDetail.setProductQuantity(quoteDetailEntity.getProductQuantity().toString());
            quoteDetail.setProductUnit(quoteDetailEntity.getProductUnit());
            quoteDetail.setProductCustomUnitPrice(Common.DECIMAL_FORMAT.format(quoteDetailEntity.getProductCustomUnitPrice()));
            quoteDetail.setProductCustomAmount(Common.DECIMAL_FORMAT.format(quoteDetailEntity.getProductCustomAmount()));
            quoteDetails.add(quoteDetail);
        }
        return quoteDetails;
    }

    // 讀取來源
    private InputStream getInputStream(Resource resource){
        try{
            return resource.getInputStream();
        }catch (Exception ex){
            ex.printStackTrace();
            throw new BusinessException(AuthEnum.A00005);
        }
    }

    private void writeFile(InputStream inputStream){
        try{
            HttpServletResponse response = HttpUtil.getResponse();
            response.setContentType(Common.EXCEL_CONTENT_TYPE);
            response.setHeader("Content-Disposition", "attachment;");
            byte[] buffer = new byte[1024];
            int bytesRead;
            ServletOutputStream outputStream = response.getOutputStream();
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();
        }catch (Exception ex){
            ex.printStackTrace();
            throw new BusinessException(AuthEnum.A00005);
        }
    }

}
