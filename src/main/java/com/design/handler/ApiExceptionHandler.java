package com.design.handler;

import com.design.base.ResponseBody;
import com.design.base.eunms.AuthEnum;
import com.design.base.eunms.ReturnEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Set;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ApiExceptionHandler {

    private final HttpServletRequest request;

    /**
     * HTTP METHOD錯誤
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(
            final HttpRequestMethodNotSupportedException e) {
        log.error("error", e);
        return ResponseEntity
                .badRequest()
                .body(
                        new ResponseBody<>(AuthEnum.A00002)
                );
    }

    /**
     * Header錯誤
     *
     * @param e
     * @return
     * */
    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleMissingRequestHeaderException(
            final MissingRequestHeaderException e) {
        log.error("error", e);
        return ResponseEntity
                .badRequest()
                .body(
                        new ResponseBody<>(AuthEnum.A00003)
                );
    }

    /**
     * 參數錯誤
     *
     * @param e
     * @return
     * */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleBindException(final BindException e) {
        log.error("error", e);
        List<FieldError> fe = e.getFieldErrors();
        StringBuffer sb = new StringBuffer();
        for (FieldError fieldError : fe) {
            sb.append(fieldError.getField())
                    .append(":")
                    .append(fieldError.getDefaultMessage())
                    .append(";");
        }
        return ResponseEntity
                .badRequest()
                .body(
                        new ResponseBody<>(AuthEnum.A00004, sb.toString())
                );
    }

    /**
     * 參數錯誤
     *
     * @param e
     * @return
     * */
    @ExceptionHandler(InvalidParamException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleInvalidParamRequest(final InvalidParamException e) {
        log.error("error", e);
        Errors errors = e.getErrors();
        List<FieldError> fieldErrors = errors.getFieldErrors();
        StringBuffer sb = new StringBuffer();
        for (FieldError fieldError : fieldErrors) {
            sb.append(fieldError.getField() + ":" + fieldError.getDefaultMessage());
        }
        return ResponseEntity
                .badRequest()
                .body(
                        new ResponseBody<>(AuthEnum.A00004, sb.toString())
                );
    }

    /**
     * 不明的錯誤
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(final BusinessException e) {
        log.error("error", e);
        return ResponseEntity
                .badRequest()
                .body(
                        new ResponseBody<>((ReturnEnum) e.getStatus())
                );
    }

    /**
     * Parameter validation failed
     *
     * @param e 輸入中文問題解決了
     * @return
     * */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleConstraintViolationException(
            final ConstraintViolationException e) {
        log.error("error", e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuffer sb = new StringBuffer();
        for (ConstraintViolation<?> constraintViolation : violations) {
            Path p = constraintViolation.getPropertyPath();
            String fieldPath = p.toString();
            String[] fieldPathSplit = fieldPath.split("\\.");
            String field = fieldPathSplit[fieldPathSplit.length - 1];
            sb.append(field).append(":").append(constraintViolation.getMessage()).append(" ; ");
        }
        return ResponseEntity
                .badRequest()
                .body(
                        new ResponseBody<>(AuthEnum.A00004, sb.toString())
                );
    }

    /**
     * 系統錯誤
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(final Exception e) {
        log.error("error", e);
        return ResponseEntity
                .badRequest()
                .body(
                        new ResponseBody<>(AuthEnum.A00005)
                );
    }

    /**
     * 對外API
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ApiException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<?> handleApiException(final ApiException e) {
        log.error("error", e);
        return ResponseEntity
                .unprocessableEntity()
                .body(
                        new ResponseBody<>(e.getCode(), e.getMessage())
                );
    }

    /**
     * 請球錯誤
     *
     * @param e 請求錯誤
     * @return
     * */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleHttpMessageNotReadableException(
            final HttpMessageNotReadableException e) {
        log.error("error", e);
        return ResponseEntity
                .badRequest()
                .body(
                        new ResponseBody(AuthEnum.A00004)
                );
    }

    private String getErrorLogUrl() {
        return "URL:" + request.getRequestURL() + "\t";
    }

}