package com.example.template.delivery;

import com.example.template.book.BookService;
import com.example.template.rental.KafkaProcessorRental;
import com.example.template.rental.RentSaved;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class DeliveryPolicyHandler {

    @Autowired
    DeliveryService deliveryService;

    @StreamListener(KafkaProcessorRental.rentSaved_INPUT)
    public void onEventByRentSaved(@Payload RentSaved rentSaved){
        System.out.println("RentalSaved");
        if("RentSaved".equals(rentSaved.getEventType()) ){
            //  수량 업데이트
            deliveryService.save(rentSaved.getDelivery());
        }
    }


}
