package com.example.template.rental;

import com.example.template.book.Book;
import com.example.template.book.BookRepository;
import com.example.template.book.BookService;
import com.example.template.delivery.DeliveryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class RentalServiceImpl implements RentalService {

    @Autowired
    BookService bookservice;

    @Autowired
    RentalRepository rentalRepository;

    public Rental getRentalById(long rentalId){
        Optional<Rental> rentalOptional = rentalRepository.findById(rentalId);
        Rental rental = rentalOptional.get();
        return rental;
    }

    public Rental save(String data){
        ObjectMapper mapper = new ObjectMapper();
        Rental rental = null;
        try {
            rental = mapper.readValue(data, Rental.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rentalRepository.save(rental);
    }

    public Rental save(Rental data){
        return rentalRepository.save(data);
    }
}
