package com.example.template.delivery;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

//@FeignClient(name ="delivery-api", url="${api.url.delivery}", fallback =DeliveryService.class)
//@FeignClient(name ="delivery-api", url="http://localhost:8088")
public interface DeliveryService {

    public Delivery getDeliveryById(Long id);
    public Delivery save(Delivery data);
    public Delivery save(String data);

    @GetMapping(value = "/delivery/{id}")
    Delivery get(@PathVariable("id") long id);

    @RequestMapping(method = RequestMethod.POST, value = "/delivery", consumes = "application/json")
    void startDelivery(@RequestParam(name="delivery") String delivery);


//    void updateDelivery(Long deliveryId, Delivery delivery);
}
