package com.luv2code.inventory.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(name = "inventory")
public class Inventory {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;

    @PositiveOrZero
    @Column(name = "stock_quantity")
    private int quantityInStock;

    @NotNull
    @PositiveOrZero
    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "available")
    private boolean available;

    @Builder
    public Inventory(UUID productId, int quantityInStock, BigDecimal price, boolean available) {
        this.productId = productId;
        this.quantityInStock = quantityInStock;
        this.price = price;
        this.available = available;
    }

    public void updateProductQuantityStock(int productQuantity) {
        if (this.quantityInStock - productQuantity < 0) {
            throw new IllegalArgumentException("Cannot have less then 0 product in inventory.");
        }

        this.quantityInStock = productQuantity;
    }
}
