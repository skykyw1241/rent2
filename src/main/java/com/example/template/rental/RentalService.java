package com.example.template.rental;

import org.springframework.stereotype.Service;

@Service
public interface RentalService {
    Rental getRentalById(long rentalId);
    Rental save(Rental data);
    Rental save(String data);
}
