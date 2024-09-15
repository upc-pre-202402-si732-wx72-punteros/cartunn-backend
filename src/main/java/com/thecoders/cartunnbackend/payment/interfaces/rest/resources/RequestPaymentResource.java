package com.thecoders.cartunnbackend.payment.interfaces.rest.resources;

public record RequestPaymentResource(String cardNumber,
                                     String expirationDate,
                                     String cardHolder,
                                     String cvc,
                                     String methodPay) {
}
