package com.thecoders.cartunnbackend.payment.interfaces.rest;

import com.thecoders.cartunnbackend.payment.domain.model.aggregates.Payment;
import com.thecoders.cartunnbackend.payment.domain.model.commands.CreatePaymentCommand;
import com.thecoders.cartunnbackend.payment.domain.model.commands.UpdatePaymentCommand;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

    @Mock
    private PaymentCommandService paymentCommandService;

    @Mock
    private PaymentQueryService paymentQueryService;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPayment() {
        CreatePaymentResource createPaymentResource = new CreatePaymentResource("1234567890123456", "12/23", "John Doe", "123", "credit");
        CreatePaymentCommand createPaymentCommand = new CreatePaymentCommand("1234567890123456", "12/23", "John Doe", "123", "credit");
        Payment payment = new Payment("1234567890123456", "12/23", "John Doe", "123", "credit");

        when(paymentCommandService.handle(any(CreatePaymentCommand.class))).thenReturn(1L);
        when(paymentQueryService.handle(any(GetPaymentByIdQuery.class))).thenReturn(Optional.of(payment));

        ResponseEntity<PaymentResource> response = paymentController.createPayment(createPaymentResource);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getPayment() {
        Payment payment = new Payment("1234567890123456", "12/23", "John Doe", "123", "credit");

        when(paymentQueryService.handle(any(GetPaymentByIdQuery.class))).thenReturn(Optional.of(payment));

        ResponseEntity<PaymentResource> response = paymentController.getPayment(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getAllPayments() {
        Payment payment = new Payment("1234567890123456", "12/23", "John Doe", "123", "credit");

        when(paymentQueryService.handle(any(GetAllPaymentsQuery.class))).thenReturn(List.of(payment));

        ResponseEntity<List<PaymentResource>> response = paymentController.getAllPayments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updatePayment() {
        UpdatePaymentResource updatePaymentResource = new UpdatePaymentResource("1234567890123456", "12/23", "John Doe", "123", "credit");
        UpdatePaymentCommand updatePaymentCommand = new UpdatePaymentCommand(1L, "1234567890123456", "12/23", "John Doe", "123", "credit");
        Payment payment = new Payment("1234567890123456", "12/23", "John Doe", "123", "credit");

        when(paymentCommandService.handle(any(UpdatePaymentCommand.class))).thenReturn(Optional.of(payment));

        ResponseEntity<PaymentResource> response = paymentController.updatePayment(1L, updatePaymentResource);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}