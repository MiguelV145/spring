package ec.edu.ups.icc.fundamentos01.exception.domain;

import org.springframework.http.HttpStatus;

import ec.edu.ups.icc.fundamentos01.exception.base.ApplicationException;

public class BadRequestException extends ApplicationException{
    
    public BadRequestException(String message){
        super(HttpStatus.BAD_REQUEST, message);
    }
    
}
