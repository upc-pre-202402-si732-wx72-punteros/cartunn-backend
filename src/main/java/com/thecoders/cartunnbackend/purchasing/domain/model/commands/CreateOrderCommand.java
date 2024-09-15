package com.thecoders.cartunnbackend.purchasing.domain.model.commands;

import java.time.LocalDate;

public record CreateOrderCommand(String name, String description, int code, LocalDate entryDate, LocalDate exitDate, String status) {
}