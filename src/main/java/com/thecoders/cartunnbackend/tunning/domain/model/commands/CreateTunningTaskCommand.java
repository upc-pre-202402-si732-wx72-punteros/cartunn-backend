package com.thecoders.cartunnbackend.tunning.domain.model.commands;

import java.time.LocalDate;

public record CreateTunningTaskCommand(String modifiedPart, LocalDate date, String status) {
}
