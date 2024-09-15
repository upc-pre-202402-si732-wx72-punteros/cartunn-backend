package com.thecoders.cartunnbackend.tunning.interfaces.rest.resources;

import java.time.LocalDate;

public record UpdateTunningTaskResource(String modifiedPart, LocalDate date, String status) {
}
