package com.example.template.book;

import com.example.template.rental.KafkaProcessorRental;
import com.example.template.rental.RentSaved;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class BookPolicyHandler {

    @Autowired
    BookService bookService;

    @StreamListener(KafkaProcessorRental.rentSaved_INPUT)
    public void onEventByRentSaved(@Payload RentSaved rentSaved){
        System.out.println("RentalSaved");
        if("RentSaved".equals(rentSaved.getEventType()) ){
            //  수량 업데이트
            bookService.decreaseStock(rentSaved.getBookId(), rentSaved.getQty());
        }
    }


}
