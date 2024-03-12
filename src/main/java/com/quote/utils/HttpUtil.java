package com.quote.utils;

import com.quote.base.Common;
import com.quote.base.eunms.AuthEnum;
import com.quote.base.eunms.ReturnEnum;
import com.quote.handler.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class HttpUtil {

    public static HttpServletRequest getRequest(){
        return getAttribute().getRequest();
    }

    public static HttpServletResponse getResponse(){
        return getAttribute().getResponse();
    }

    public static <T extends ReturnEnum> void write(T t){
        try{
            HttpServletResponse response = getResponse();
            StringBuilder sb = new StringBuilder();
            sb.append("{\"code\":\"");
            sb.append(t.key());
            sb.append("\",\"message\":\"");
            sb.append(t.value());
            sb.append("\"}");
            response.setCharacterEncoding(Common.ENCODING);
            response.setContentType(Common.CONTENT_TYPE);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(sb.toString());
            response.getWriter().flush();
        }catch (Exception ex){
            ex.printStackTrace();
            throw new BusinessException(AuthEnum.A00001);
        }
    }

    private static ServletRequestAttributes getAttribute(){
        return (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    }

}