package com.LaptopWeb.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String orderCode;

    @Column(nullable = false)
    String address;

    Date createdAt;

    Long totalPrice;

    @Column(columnDefinition = "MEDIUMTEXT")
    String note;

    boolean paymentStatus;

    String paymentType;


    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "status_order_id")
    StatusOrder statusOrder;



    @PrePersist
    public void generateOrderCode() {
        if (this.orderCode == null) {
            this.orderCode = UUID.randomUUID().toString();
        }
    }
}
