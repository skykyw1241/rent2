package com.example.template.delivery;

import com.example.template.book.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class DeliveryServiceImpl implements DeliveryService{

    @Autowired
    DeliveryRepository deliveryRepository;

    /**
     * 배송 시작
     */
    public void startDelivery(String delivery){
        System.out.println("DeliveryServiceImpl.startDelivery() start");
        this.save(delivery);

        //  비동기 호출 필요
        //  rental에 update 필요
    }


    public Delivery getDeliveryById(Long id){
        System.out.println("DeliveryServiceImpl.getDeliveryById() start");
        Optional<Delivery> deliveryOptional = deliveryRepository.findById(id);
        Delivery delivery = deliveryOptional.get();
        return delivery;
    }

    public Delivery save(String data){
        System.out.println("DeliveryServiceImpl.save() start");
        ObjectMapper mapper = new ObjectMapper();
        Delivery delivery = null;
        try {
            delivery = mapper.readValue(data, Delivery.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return deliveryRepository.save(delivery);
    }

    @Override
    public Delivery get(long id) {
        return this.getDeliveryById(id);
    }

    public Delivery save(Delivery data){
        System.out.println("DeliveryServiceImpl.save() start");
        return deliveryRepository.save(data);
    }
}
