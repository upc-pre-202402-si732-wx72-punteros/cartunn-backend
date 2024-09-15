package com.thecoders.cartunnbackend.product.domain.model.aggregates;

import com.thecoders.cartunnbackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
public class Favorite  extends AuditableAbstractAggregateRoot<Favorite> {

    @Getter
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    public Favorite() {
    }
    public Favorite(Product product) {
        this.product = product;
    }
}


