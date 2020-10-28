package com.example.template.book;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Service
//@FeignClient(name ="book", url="${api.url.book}")
public interface BookService {
    //void decreaseStock(Long bookId, int quantity);
    //void increaseStock(Long bookId, int quantity);
//    @RequestMapping(method = RequestMethod.PUT, value = "/book/decreaseStock/{bookId}", consumes = "application/json")
    void decreaseStock(@PathVariable("bookId") Long bookId, int qty);

    //@RequestMapping(method = RequestMethod.PUT, value = "/book/increaseStock/{bookId}", consumes = "application/json")
    //void increaseStock(@PathVariable("bookId") Long bookId, int qty);

    Book getBookById(Long id);
    Book save(String data);
}
