package com.thecoders.cartunnbackend.payment.domain.model.aggregates;

import com.thecoders.cartunnbackend.payment.domain.model.commands.CreateCartCommand;
import com.thecoders.cartunnbackend.product.domain.model.aggregates.Product;
import jakarta.persistence.*;
import lombok.Getter;
import com.thecoders.cartunnbackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
public class Cart extends AuditableAbstractAggregateRoot<Cart>{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;
    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "cart_product",
    joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> assignedProducts= new HashSet<>();
    public Cart() {
        this.total = BigDecimal.ZERO;
        this.payment = new Payment();
    }

    public Cart(BigDecimal total, Payment payment,Set<Product> products) {
        this();
        this.total = total;
        this.payment=payment;
        this.assignedProducts=products;

    }
    public Cart(CreateCartCommand command) {
        this();
        this.total = command.total();
        this.payment=command.payment();
        this.assignedProducts=command.products();
    }
    public Cart updateInformation(BigDecimal total, Payment payment,Set<Product> products) {
        this.total = total;
        this.payment=payment;
        //for(int i=0;i<products.size())
        this.assignedProducts=products;
        return this;
    }
}
