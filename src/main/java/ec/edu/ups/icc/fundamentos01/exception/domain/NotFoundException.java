package ec.edu.ups.icc.fundamentos01.exception.domain;

import org.springframework.http.HttpStatus;

import ec.edu.ups.icc.fundamentos01.exception.base.ApplicationException;

public class NotFoundException extends ApplicationException {

    public NotFoundException(String message)
{
    super(HttpStatus.NOT_FOUND, message);
}    
}
