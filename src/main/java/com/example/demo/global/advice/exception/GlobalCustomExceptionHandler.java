package com.example.demo.global.advice.exception;

import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Order(1)
@Slf4j
public class GlobalCustomExceptionHandler {


    //커스텀 익셉션 사용 예
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResult> validException(CustomException e) {
        return ErrorResult.ofResponse(e.getErrorCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> validException(MethodArgumentNotValidException e) {
        String resultData = getFieldError(e);
        return new ResponseEntity<>(resultData, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResult> nullPointException(NullPointerException e) {
        return ErrorResult.ofResponse(e, HttpStatus.valueOf(500));
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleCustomException(NoSuchElementException e) {
        return new ResponseEntity<>(makeResult(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleCustomException(IllegalArgumentException e) {
        return new ResponseEntity<>(makeResult(e), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleCustomException(IOException e) {
        return new ResponseEntity<>(makeResult(e), HttpStatus.BAD_REQUEST);
    }

    public String makeResult(Exception e){
        e.printStackTrace();
        log.warn(e.getMessage());
        log.warn("custom exception by junhyuk. you can check this in GlobalCustomDetailExceptionHandler.java");
        return e.getMessage();
    }


    public String getFieldError( MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        StringBuilder errMessage = new StringBuilder();

        for (FieldError error : result.getFieldErrors()) {
            errMessage.append("[")
                .append(error.getField())
                .append("] ")
                .append(":")
                .append(error.getDefaultMessage())
                .append("\n");
        }
        log.warn("custom exception handling. please check GlobalCustomExceptionHandler.java");
        return errMessage.toString();
    }
}