package com.example.template.rental;

import com.example.template.delivery.Delivery;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//@FeignClient(name ="delivery", url="${api.url.delivery}")
@FeignClient(name ="delivery", url="http://localhost:8088")
public interface DeliveryService {

    public Delivery getDeliveryById(Long id);
    public Delivery save(Delivery data);
    public Delivery save(String data);


    @RequestMapping(method = RequestMethod.POST, value = "/delivery", consumes = "application/json")
    void startDelivery(Delivery delivery);


//    void updateDelivery(Long deliveryId, Delivery delivery);
}
