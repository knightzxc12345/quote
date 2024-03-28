package com.design.usecase.quote;

import com.design.base.eunms.AuthEnum;
import com.design.entity.quote.QuoteEntity;
import com.design.entity.quote_detail.QuoteDetailEntity;
import com.design.handler.BusinessException;
import com.design.modle.QuoteDetail;
import com.design.service.customer.CustomerService;
import com.design.service.quote.QuoteService;
import com.design.service.quote_detail.QuoteDetailService;
import com.design.service.user.UserService;
import com.design.utils.ExcelUtil;
import com.design.utils.HttpUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuoteFileUseCaseImpl implements QuoteFileUseCase {

    @Value("classpath:files/quote-01.xlsx")
    private Resource quote01Resource;

    @Value("classpath:files/quote-02.xlsx")
    private Resource quote02Resource;

    private final QuoteService quoteService;

    private final QuoteDetailService quoteDetailService;

    private final CustomerService customerService;

    private final UserService userService;

    @Override
    public void preview(String quoteUuid) {
        QuoteEntity quoteEntity = quoteService.findByUuid(quoteUuid);
        List<QuoteDetailEntity> quoteDetailEntities = quoteDetailService.findAll(quoteUuid);
        List<QuoteDetail> quoteDetails = getQuoteDetails(quoteDetailEntities);
        InputStream originQuote01InputStream = getInputStream(quote01Resource);
        InputStream quoteInputStream = ExcelUtil.create(originQuote01InputStream, quoteDetails);
//        writeExcelToFile(quoteInputStream);
        writeFile(quoteInputStream);
    }

    private List<QuoteDetail> getQuoteDetails(List<QuoteDetailEntity> quoteDetailEntities){
        List<QuoteDetail> quoteDetails = new ArrayList<>();
        if(null == quoteDetails || quoteDetails.isEmpty()){
            return quoteDetails;
        }
        DecimalFormat formatter = new DecimalFormat("#,###");
        QuoteDetail quoteDetail;
        Integer index = 1;
        for(QuoteDetailEntity quoteDetailEntity : quoteDetailEntities){
            quoteDetail = new QuoteDetail();
            quoteDetail.setIndex(index++);
            quoteDetail.setProductNo(quoteDetailEntity.getProductNo());
            quoteDetail.setProductItemName(quoteDetailEntity.getItemName());
            quoteDetail.setProductSpecification(quoteDetailEntity.getProductSpecification());
            quoteDetail.setProductQuantity(quoteDetailEntity.getProductQuantity().toString());
            quoteDetail.setProductUnit(quoteDetailEntity.getProductUnit());
            quoteDetail.setProductCustomUnitPrice(formatter.format(quoteDetailEntity.getProductCustomUnitPrice()));
            quoteDetail.setProductCustomAmount(formatter.format(quoteDetailEntity.getProductCustomAmount()));
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

    public static byte[] toByteArray(InputStream inputStream){
        try{
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[16384];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            return buffer.toByteArray();
        }catch (Exception ex){
            ex.printStackTrace();
            throw new BusinessException(AuthEnum.A00005);
        }
    }

    private void writeFile(InputStream inputStream){
        try{
            HttpServletResponse response = HttpUtil.getResponse();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=test.xlsx");
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                response.getOutputStream().write(buffer, 0, bytesRead);
            }
            inputStream.close();
            response.getOutputStream().flush();
        }catch (Exception ex){
            ex.printStackTrace();
            throw new BusinessException(AuthEnum.A00005);
        }
    }

    public static void writeExcelToFile(InputStream excelInputStream){
        Path directory = Paths.get("D:\\Downloads");
        Path filePath = directory.resolve("test.xlsx");
        try{
            Workbook workbook = new XSSFWorkbook(excelInputStream);
            FileOutputStream outputStream = new FileOutputStream(filePath.toFile());
            workbook.write(outputStream);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
