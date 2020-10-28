package com.example.template.rental;

import com.example.template.Application;
import com.example.template.book.Book;
import com.example.template.delivery.Delivery;
import com.example.template.book.BookRepository;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Table(name = "rental_table")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long bookId;
    private String bookName;
    private int qty;
    private int price;
    private String customerId;
    private String customerName;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    @ManyToOne
    @JoinColumn(name = "book_idx", nullable = false, updatable = false)
    private Book book;

    //private Delivery delivery;

//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
//    @PrimaryKeyJoinColumn
//    private Delivery delivery;

    @PrePersist
    private void bookCheck(){
        if( bookId == null ){
            throw new RuntimeException();
        }

        try {
            Thread.currentThread().sleep((long) (800 + Math.random() * 220));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int price = 0;
        String productName = null;

        BookRepository bookRepository = Application.applicationContext.getBean(BookRepository.class);
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        Book book = bookOptional.get();

        price = book.getPrice();
        productName = book.getName();
        if( book.getStock() < getQty()){
            throw new RentalException("재고부족");
        }

        this.setPrice(price);
        this.setBookName(productName);
        this.setBook(book);
    }

    /**
     * 주문이 들어옴
     */
    @PostPersist
    private void onPostPersist() throws Exception {
        
        // 1. 상품 서비스에 수량을 직접 변경하여 JPA call
        RestTemplate restTemplate = Application.applicationContext.getBean(RestTemplate.class);
        Environment env = Application.applicationContext.getEnvironment();
        String bookUrl = env.getProperty("api.url.book") + "/book/" + bookId;
        System.out.println(bookUrl);

        // 상품 서비스의 재고량 조회
        ResponseEntity<Book> bookEntity = restTemplate.getForEntity(bookUrl, Book.class);
        Book book = bookEntity.getBody();

        int stock = book.getStock();
        // 재고량 변경
        int remainStock = stock - qty;
        book.setStock(remainStock);
        String modifyStockUrl = env.getProperty("api.url.book") + "/book/decreaseStock";

        Delivery delivery = new Delivery();
        delivery.setQty(this.getQty());
        delivery.setCustomerId(this.getCustomerId());
        delivery.setCustomerName(this.getCustomerName());
        delivery.setRentId(this.getId());

        try{
            System.out.println("restTemplate.patchForObject() start");
            String result = restTemplate.patchForObject(modifyStockUrl , book, String.class);
            System.out.println("상품 수량 변경 api call " + stock + " => " + remainStock);

        // 배송 시작
        // 1. 상품 서비스에 수량을 직접 변경하여 JPA call
            System.out.println("restTemplate.patchForObject() start");
            String createDeliveryUrl = env.getProperty("api.url.delivery") + "/delivery";
            String resultDelv = restTemplate.postForObject(createDeliveryUrl , delivery, String.class);
        }catch(Exception ex){

            RentSaved bookRentSaved = new RentSaved();
            bookRentSaved.setId(this.id);
            bookRentSaved.setBookId(this.bookId);
            bookRentSaved.setQty(this.qty);
            bookRentSaved.setCustomerId(this.customerId);
            bookRentSaved.setDelivery(delivery);


            System.out.println("bookRentSaved.publish() start");
            bookRentSaved.publish();        //  비동기식
            System.out.println("bookRentSaved.publish() end");
        }
        /*DeliveryService deliveryService = Application.applicationContext.getBean(DeliveryService.class);

        try{
            System.out.println("deliveryService.startDelivery");
            deliveryService.startDelivery(delivery);    //  동기식
        }catch(Exception e){
            e.printStackTrace();
        }*/

    }

//    @PreUpdate
//    private void callDeliveryStop(){
//        /**
//         * 주문이 취소됨
//         */
//        if( "OrderCancelled".equals(this.getState())){
//            System.out.println("this.getState() = " + this.getState());
//            // 배송 취소
//            DeliveryService deliveryService = Application.applicationContext.getBean(DeliveryService.class);
//            delivery.setDeliveryState(DeliveryStatus.DeliveryCancelled.name());
//            deliveryService.updateDelivery(delivery.getDeliveryId(), delivery);
//        }
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    /*public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }*/
}
