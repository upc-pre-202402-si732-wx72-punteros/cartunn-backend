package com.thecoders.cartunnbackend.product.domain.model.aggregates;

import com.thecoders.cartunnbackend.payment.domain.model.aggregates.Cart;
import com.thecoders.cartunnbackend.product.domain.model.commands.CreateProductCommand;
import com.thecoders.cartunnbackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
public class Product extends AuditableAbstractAggregateRoot<Product> {

    private String title;
    private String description;
    private String image;
    private Double price;

    @ManyToMany(mappedBy = "assignedProducts")
    private Set<Cart> cartSet = new HashSet<>();
    public Product() {
        this.title = Strings.EMPTY;
        this.description = Strings.EMPTY;
        this.image = Strings.EMPTY;
        this.price = 0.0;
    }

    public Product(String title, String description, String image, Double price) {
            this();
            this.title = title;
            this.description = description;
            this.image = image;
            this.price = price;
    }
    public Product(CreateProductCommand command) {
        this();
        this.title = command.title();
        this.description = command.description();
        this.image = command.image();
        this.price = command.price();
    }
    public Product updateInformation(String title, String description, String image, Double price) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.price = price;
        return this;
    }
}

