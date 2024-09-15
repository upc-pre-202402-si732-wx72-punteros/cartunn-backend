package com.thecoders.cartunnbackend.payment.interfaces.rest.transform;


import com.thecoders.cartunnbackend.payment.domain.model.commands.UpdatePaymentCommand;
import com.thecoders.cartunnbackend.payment.interfaces.rest.resources.UpdatePaymentResource;

public class UpdatePaymentCommandFromResourceAssembler {
    public static UpdatePaymentCommand toCommandFromResource(Long paymentId, UpdatePaymentResource resource){
        return new UpdatePaymentCommand(paymentId,
                resource.cardNumber(),
                resource.expirationDate(),
                resource.cardHolder(),
                resource.cvc(),
                resource.methodPay());

    }
}