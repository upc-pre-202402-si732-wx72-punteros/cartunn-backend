package com.thecoders.cartunnbackend.purchasing.domain.model.aggregates;

import com.thecoders.cartunnbackend.purchasing.domain.model.commands.CreateOrderCommand;
import com.thecoders.cartunnbackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "code", nullable = false, length = 4)
    private int code;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "exit_date", nullable = false)
    private LocalDate exitDate;

    @Column(name = "status", nullable = false)
    private String status;

    public Order() {
        this.name = Strings.EMPTY;
        this.description = Strings.EMPTY;
        this.code = 0;
        this.entryDate = LocalDate.now();
        this.exitDate = LocalDate.now();
        this.status = Strings.EMPTY;
    }

    public Order(String order, String description, int code, LocalDate entryDate, LocalDate exitDate, String status) {
        this();
        this.name = order;
        this.description = description;
        this.code = code;
        this.entryDate = entryDate;
        this.exitDate = exitDate;
        this.status = status;
    }

    public Order(CreateOrderCommand command) {
        this();
        this.name = command.name();
        this.description = command.description();
        this.code = command.code();
        this.entryDate = command.entryDate();
        this.exitDate = command.exitDate();
        this.status = command.status();
    }

    public Order updateInformation(String order, String description, int code, LocalDate entryDate, LocalDate exitDate, String status) {
        this.name = order;
        this.description = description;
        this.code = code;
        this.entryDate = entryDate;
        this.exitDate = exitDate;
        this.status = status;
        return this;
    }
}