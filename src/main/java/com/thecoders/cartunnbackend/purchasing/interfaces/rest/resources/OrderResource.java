package com.thecoders.cartunnbackend.purchasing.interfaces.rest.resources;

import java.time.LocalDate;

public record OrderResource(Long id, String name, String description, int code, LocalDate entryDate, LocalDate exitDate, String status) {
}