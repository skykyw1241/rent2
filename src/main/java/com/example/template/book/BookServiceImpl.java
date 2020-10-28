package com.example.template.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository BookRepository;

    public void decreaseStock(Long bookId, int quantity) {

        /**
         * 주문이 발생시, 수량을 줄인다.
         */
        Optional<Book> productOptional = BookRepository.findById(bookId);
        Book book = productOptional.get();
        book.setStock(book.getStock() - quantity);
        System.out.println("bookId="+bookId+" , stock="+book.getStock()+" , quantity="+quantity);
        BookRepository.save(book);
    }

    public void increaseStock(Long bookId, int quantity) {
        /**
         * 주문 취소시, 수량을 늘인다
         */
        Optional<Book> productOptional = BookRepository.findById(bookId);
        Book product = productOptional.get();
        product.setStock(product.getStock() + quantity);

        BookRepository.save(product);
    }

    /**
     * 상품 조회
     */
    public Book getBookById(Long id){

        Optional<Book> productOptional = BookRepository.findById(id);
        Book book = productOptional.get();
        System.out.println(book.getId()+":"+book.getStock());
        return book;
    }

    public Book save(String data){
        ObjectMapper mapper = new ObjectMapper();
        Book book = null;
        try {
            book = mapper.readValue(data, Book.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return BookRepository.save(book);
    }
}
