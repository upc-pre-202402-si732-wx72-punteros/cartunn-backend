package com.thecoders.cartunnbackend.payment.interfaces.rest.transform;


import com.thecoders.cartunnbackend.payment.domain.model.commands.CreatePaymentCommand;
import com.thecoders.cartunnbackend.payment.interfaces.rest.resources.CreatePaymentResource;

public class CreatePaymentCommandFromResourceAssembler {
    public static CreatePaymentCommand toCommandFromResource(CreatePaymentResource resource){
        return new CreatePaymentCommand(
                resource.cardNumber(),
                resource.expirationDate(),
                resource.cardHolder(),
                resource.cvc(),
                resource.methodPay()
        );
    }
}
