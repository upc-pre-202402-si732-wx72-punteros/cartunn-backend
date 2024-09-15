package com.thecoders.cartunnbackend.payment.interfaces.rest.resources;



import java.math.BigDecimal;
import java.util.Set;

public record RequestCartResource(BigDecimal total,
                                  CreatePaymentResource payment,
                                  Set<Long> productIds) {
}
