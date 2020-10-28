package com.example.template.delivery;

import com.example.template.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@RestController
public class DeliveryController {

    @Autowired
    DeliveryService deliveryService;

    @GetMapping("/delivery/{deliveryId}")
    Delivery selectDelivery(@PathVariable(value = "deliveryId") Long deliveryId) {
        return  this.deliveryService.getDeliveryById(deliveryId);
    }

    @PostMapping("/cancel/{deliveryId}")
    Delivery canceledDelivery(@PathVariable(value = "deliveryId") Long deliveryId) {
        Delivery delivery = deliveryService.getDeliveryById(deliveryId);


        return  this.deliveryService.getDeliveryById(deliveryId);
    }

    @PostMapping("/delivery")
    Delivery insertDelivery(@RequestBody String data) {

        return this.deliveryService.save(data);
    }

    //@Value("${superuser.userId}")
    //String superUserId;

    @Autowired
    Environment env;
    @GetMapping("/env")
    String getEnvSample() {

        // 1. applicationContext 에서 가져오기
        Environment envContext = Application.applicationContext.getEnvironment();
        System.out.println(" applicationContext = " + envContext.getProperty("superuser.userId"));

        // 2. Autowired Environment
        System.out.println(" Autowired Environment = " + env.getProperty("superuser.userId"));

        // 3. @Value
        //System.out.println(" @Value = " + superUserId);

        return env.getProperty("superuser.userId");
    }
}
