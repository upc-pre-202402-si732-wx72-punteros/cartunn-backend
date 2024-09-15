package com.thecoders.cartunnbackend.payment.domain.model.commands;

public record UpdatePaymentCommand(
        Long paymentId,
        String cardNumber,
        String expirationDate,
        String cardHolder,
        String cvc,
        String methodPay
) {
}
