package com.example.template.delivery;

import com.example.template.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    //List<Delivery> findByOrderIdOrderByDeliveryIdDesc(Long deliveryId);

}
