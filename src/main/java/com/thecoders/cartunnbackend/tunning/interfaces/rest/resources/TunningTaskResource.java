package com.thecoders.cartunnbackend.tunning.interfaces.rest.resources;


import java.time.LocalDate;

public record TunningTaskResource(Long id, String modifiedPart, LocalDate date, String status) {
}
