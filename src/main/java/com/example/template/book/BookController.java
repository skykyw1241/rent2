package com.example.template.book;

import com.example.template.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping("/book/{bookId}")
    Book productStockCheck(@PathVariable(value = "bookId") Long bookId) {
        return  this.bookService.getBookById(bookId);
    }

    @PostMapping("/book")
    Book bookInsert(@RequestBody String data) {
        return this.bookService.save(data);
    }

    @RequestMapping(method = RequestMethod.PUT, path="/book/{book}", consumes = "application/json")
    Book bookModify(@PathVariable(value = "book") String book) {
        return this.bookService.save(book);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/book/decreaseStock", consumes = "application/json")
    Book updateBookStock(@RequestBody String data) {
        return this.bookService.save(data);
    }

    /*@Autowired
    Environment env;
    *//**
     * env 혹은 설정 값을 가져오는 테스트 코드
     *//*
    @GetMapping("/env")
    String getEnvSample() {

        // 1. applicationContext 에서 가져오기
        Environment envContext = Application.applicationContext.getEnvironment();
        System.out.println(" applicationContext = " + envContext.getProperty("superuser.userId"));

        // 2. Autowired Environment
        System.out.println(" Autowired Environment = " + env.getProperty("superuser.userId"));

        // 3. @Value
        System.out.println(" @Value = " + superUserId);

        return superUserId;
    }*/
}
