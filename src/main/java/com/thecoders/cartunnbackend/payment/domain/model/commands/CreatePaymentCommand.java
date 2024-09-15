package com.thecoders.cartunnbackend.payment.domain.model.commands;

public record CreatePaymentCommand(String cardNumber,
                                   String expirationDate,
                                   String cardHolder,
                                   String cvc,
                                   String methodPay) {
}
