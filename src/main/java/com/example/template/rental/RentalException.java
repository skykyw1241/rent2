package com.example.template.rental;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "No Available stock!")
public class RentalException extends RuntimeException {
    public RentalException(String message) {
        super(message);
    }
}
