package com.example.sahal.Springbootmultithreading2.Exception.ExceptionAdvice;

import com.example.sahal.Springbootmultithreading2.Exception.GlobalException;
import com.example.sahal.Springbootmultithreading2.Exception.ResourceNotFoundException;
import com.example.sahal.Springbootmultithreading2.Exception.EmployeeAlreadyExistException;
import com.example.sahal.Springbootmultithreading2.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected Map<String, String> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(x -> {
                    errors.put(x.getField(),
                            x.getDefaultMessage());}
                );
        return errors;
    }

    //Handle specific exception
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleResourceNotFoundException(
            ResourceNotFoundException exception, WebRequest request){
        ErrorDto errorDto = new ErrorDto(
                "Resource not found exception",
                exception.getMessage(),
                new Date());
        return errorDto;
    }

    //Handle specific exception
    @ExceptionHandler(EmployeeAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handleEmployeeAlreadyExistException(
            EmployeeAlreadyExistException exception, WebRequest request){
        ErrorDto errorDto = new ErrorDto(
                "Employee already exist exception",
                exception.getMessage(),
                new Date());
        return errorDto;
    }

//    // handle global exceptions
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorDto handleGlobalException
//    (Exception exception, WebRequest request){
//        ErrorDto errorDto = new ErrorDto(
//                "Exception",
//                exception.getMessage(),
//                new Date());
//        return errorDto;
//    }

    // handle global exceptions
    @ExceptionHandler(GlobalException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleGlobalException
    (GlobalException exception, WebRequest request){
        ErrorDto errorDto = new ErrorDto(
                "Exception",
                exception.getMessage(),
                new Date());
        return errorDto;
    }
}
