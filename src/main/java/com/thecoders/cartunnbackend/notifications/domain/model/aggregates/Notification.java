package com.thecoders.cartunnbackend.notifications.domain.model.aggregates;

import com.thecoders.cartunnbackend.notifications.domain.model.commands.CreateNotificationCommand;
import com.thecoders.cartunnbackend.purchasing.domain.model.aggregates.Order;
import jakarta.persistence.*;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;

@Getter
@Entity
@Table(name = "notifications")
public class Notification {

    @Getter
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "description", nullable = false)
    private String description;


    public Notification() {
        this.type = Strings.EMPTY;
        this.description = Strings.EMPTY;
    }

    public Notification(Order order, String type, String description) {
        this();
        this.order = order;
        this.type = type;
        this.description = description;
    }
    public Notification(CreateNotificationCommand command, Order order) {
        this(order, command.type(), command.description());
    }

    public Notification updateInformation(String type, String description) {
        this.type = type;
        this.description = description;
        return this;
    }
}