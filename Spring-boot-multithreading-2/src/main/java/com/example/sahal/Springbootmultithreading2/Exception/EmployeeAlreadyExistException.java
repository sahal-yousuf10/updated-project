package com.example.sahal.Springbootmultithreading2.Exception;


public class EmployeeAlreadyExistException extends RuntimeException{
    public EmployeeAlreadyExistException(String message){
        super(message);
    }
}
