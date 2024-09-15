package com.thecoders.cartunnbackend.purchasing.interfaces.rest.transform;

import com.thecoders.cartunnbackend.purchasing.domain.model.aggregates.Order;
import com.thecoders.cartunnbackend.purchasing.interfaces.rest.resources.OrderResource;

public class OrderResourceFromEntityAssembler {
    public static OrderResource toResourceFromEntity(Order entity) {
        return new OrderResource(entity.getId(), entity.getName(), entity.getDescription(), entity.getCode(), entity.getEntryDate(), entity.getExitDate(), entity.getStatus());
    }
}