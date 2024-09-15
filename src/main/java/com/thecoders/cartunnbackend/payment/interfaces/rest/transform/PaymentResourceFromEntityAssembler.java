package com.thecoders.cartunnbackend.payment.interfaces.rest.transform;


import com.thecoders.cartunnbackend.payment.domain.model.aggregates.Payment;
import com.thecoders.cartunnbackend.payment.interfaces.rest.resources.PaymentResource;

public class PaymentResourceFromEntityAssembler {
    public static PaymentResource toResourceFromEntity(Payment entity){
        return new PaymentResource(
                entity.getId(),
                entity.getCardNumber(),
                entity.getExpirationDate(),
                entity.getCardHolder(),
                entity.getCvc(),
                entity.getMethodPay()
        );
    }
}
