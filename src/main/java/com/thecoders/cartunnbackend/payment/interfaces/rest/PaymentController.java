package com.thecoders.cartunnbackend.payment.interfaces.rest;

import com.thecoders.cartunnbackend.payment.domain.model.queries.GetAllPaymentsQuery;
import com.thecoders.cartunnbackend.payment.domain.model.queries.GetPaymentByIdQuery;
import com.thecoders.cartunnbackend.payment.domain.services.PaymentCommandService;
import com.thecoders.cartunnbackend.payment.domain.services.PaymentQueryService;
import com.thecoders.cartunnbackend.payment.interfaces.rest.resources.CreatePaymentResource;
import com.thecoders.cartunnbackend.payment.interfaces.rest.resources.PaymentResource;
import com.thecoders.cartunnbackend.payment.interfaces.rest.resources.UpdatePaymentResource;
import com.thecoders.cartunnbackend.payment.interfaces.rest.transform.CreatePaymentCommandFromResourceAssembler;
import com.thecoders.cartunnbackend.payment.interfaces.rest.transform.PaymentResourceFromEntityAssembler;
import com.thecoders.cartunnbackend.payment.interfaces.rest.transform.UpdatePaymentCommandFromResourceAssembler;
import com.thecoders.cartunnbackend.productRefunds.domain.model.queries.GetAllProductRefundsQuery;
import com.thecoders.cartunnbackend.productRefunds.interfaces.rest.resources.UpdateProductRefundResource;
import com.thecoders.cartunnbackend.productRefunds.interfaces.rest.transform.ProductRefundResourceFromEntityAssembler;
import com.thecoders.cartunnbackend.productRefunds.interfaces.rest.transform.UpdateProductRefundCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "api/v1/payment", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Payment ", description = "Payment Management Endpoints")

public class PaymentController {
    private final PaymentCommandService paymentCommandService;
    private final PaymentQueryService paymentQueryService;

    public PaymentController(PaymentCommandService paymentCommandService, PaymentQueryService paymentQueryService) {
        this.paymentCommandService = paymentCommandService;
        this.paymentQueryService = paymentQueryService;
    }


    @PostMapping
    public ResponseEntity<PaymentResource> createPayment(@RequestBody CreatePaymentResource createPaymentResource) {
        var createPaymentCommand = CreatePaymentCommandFromResourceAssembler.toCommandFromResource(createPaymentResource);
        var paymentId = paymentCommandService.handle(createPaymentCommand);
        if (paymentId == 0L) return ResponseEntity.badRequest().build();

        var getPaymentByIdQuery = new GetPaymentByIdQuery(paymentId);
        var payment = paymentQueryService.handle(getPaymentByIdQuery);
        if(payment.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        var paymentResource = PaymentResourceFromEntityAssembler.toResourceFromEntity(payment.get());
        return new ResponseEntity<>(paymentResource, HttpStatus.CREATED);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResource> getPayment(@PathVariable Long paymentId){
        var getPaymentByIdQuery = new GetPaymentByIdQuery(paymentId);
        var payment = paymentQueryService.handle(getPaymentByIdQuery);
        if(payment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var paymentResource = PaymentResourceFromEntityAssembler.toResourceFromEntity(payment.get());
        return ResponseEntity.ok(paymentResource);
    }

    @GetMapping
    public ResponseEntity<List<PaymentResource>> getAllPayments(){
        var getAllPaymentsQuery = new GetAllProductRefundsQuery();
        var payments = paymentQueryService.handle(new GetAllPaymentsQuery());
        var paymentsResources = payments.stream().map(PaymentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(paymentsResources);
    }

    @PutMapping("/{paymentId}")
    public ResponseEntity<PaymentResource> updatePayment(@PathVariable Long paymentId, @RequestBody UpdatePaymentResource updatePaymentResource){
        var updatePaymentCommand = UpdatePaymentCommandFromResourceAssembler.toCommandFromResource(paymentId, updatePaymentResource);
        var updatePayment = paymentCommandService.handle(updatePaymentCommand);
        if (updatePayment.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        var paymentResource = PaymentResourceFromEntityAssembler.toResourceFromEntity(updatePayment.get());
        return ResponseEntity.ok(paymentResource);
    }

}
