package com.example.template.delivery;


import javax.persistence.*;

@Entity
@Table(name = "delivery_table")
public class Delivery {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long rentId;
    private String customerId;
    private String customerName;
    private String deliveryState;
    private int qty;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRentId() {
        return rentId;
    }

    public void setRentId(Long rentId) {
        this.rentId = rentId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
//    @OneToOne(cascade = CascadeType.ALL)
//    @PrimaryKeyJoinColumn(name = "order_id", referencedColumnName = "deliveryId")
//    private Order order;


    @PostPersist
    private void PostPersionst() throws Exception {
        System.out.println("Delivery.PostPersionst() start");

        // 상품 수량 변경 - 수량변경은 상품서비스에서 직접..
        DeliveryCreated create = new DeliveryCreated();
        create.setId(this.getId());
        create.setRentId(this.getRentId());

        System.out.println("DeliveryCreated.publish() start");
        create.publish();        //  비동기식
        System.out.println("DeliveryCreated.publish() end");

    }

//    @PostUpdate
//    private void deliveryUpdate(){
//        if( "OrderCancelled".equals(order.getState())){
//            // 상품 수량 변경
//            ProductService productService = Application.applicationContext.getBean(ProductService.class);
//            productService.increaseStock(productId, quantity);
//        }
//    }

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

    public String getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(String deliveryState) {
        this.deliveryState = deliveryState;
    }

}
