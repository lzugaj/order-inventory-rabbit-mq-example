package com.luv2code.order.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "id")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_id_seq"
    )
    @SequenceGenerator(
            name = "order_id_seq",
            allocationSize = 1
    )
    private Long id;

    @NotBlank
    @Column(name = "product_name")
    private String productName;

    @NotNull
    @Enumerated(STRING)
    @Column(name = "product_category")
    private ProductCategory productCategory;

    @PositiveOrZero
    @Column(name = "quantity")
    private int quantity;

    @NotNull
    @PositiveOrZero
    @Column(name = "price")
    private BigDecimal price;

    @NotNull
    @Column(name = "order_at")
    private LocalDateTime orderAt;

    @Builder
    public Order(
            String productName,
            ProductCategory productCategory,
            int quantity,
            BigDecimal price
    ) {
        Assert.notNull(productName, "Product name must not be null.");
        Assert.notNull(productCategory, "Product category must not be null.");
        Assert.notNull(price, "Price must not be null.");

        this.productName = productName;
        this.productCategory = productCategory;
        this.quantity = quantity;
        this.price = price;
        this.orderAt = LocalDateTime.now();
    }
}
