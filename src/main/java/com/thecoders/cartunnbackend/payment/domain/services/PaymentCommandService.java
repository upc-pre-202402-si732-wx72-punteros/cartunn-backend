package com.thecoders.cartunnbackend.payment.domain.services;

import com.thecoders.cartunnbackend.payment.domain.model.aggregates.Payment;
import com.thecoders.cartunnbackend.payment.domain.model.commands.CreatePaymentCommand;
import com.thecoders.cartunnbackend.payment.domain.model.commands.DeletePaymentCommand;
import com.thecoders.cartunnbackend.payment.domain.model.commands.UpdatePaymentCommand;

import java.util.Optional;

public interface PaymentCommandService {
    Long handle(CreatePaymentCommand command);
    Optional<Payment> handle(UpdatePaymentCommand command);
    void handle(DeletePaymentCommand command);
}
