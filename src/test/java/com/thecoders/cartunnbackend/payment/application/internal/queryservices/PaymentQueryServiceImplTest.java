package com.thecoders.cartunnbackend.payment.application.internal.queryservices;

import com.thecoders.cartunnbackend.payment.domain.model.aggregates.Payment;
import com.thecoders.cartunnbackend.payment.domain.model.queries.GetAllPaymentsQuery;
import com.thecoders.cartunnbackend.payment.domain.model.queries.GetPaymentByIdQuery;
import com.thecoders.cartunnbackend.payment.infrastructure.persistence.jpa.repositories.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentQueryServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentQueryServiceImpl paymentQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleGetPaymentByIdQuery() {
        Long paymentId = 1L;
        GetPaymentByIdQuery query = new GetPaymentByIdQuery(paymentId);
        Payment payment = new Payment();
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        Optional<Payment> result = paymentQueryService.handle(query);

        assertTrue(result.isPresent());
        assertEquals(payment, result.get());
        verify(paymentRepository, times(1)).findById(paymentId);
    }

    @Test
    void handleGetAllPaymentsQuery() {
        GetAllPaymentsQuery query = new GetAllPaymentsQuery();
        Payment payment = new Payment();
        List<Payment> payments = Collections.singletonList(payment);
        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> result = paymentQueryService.handle(query);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(payment, result.get(0));
        verify(paymentRepository, times(1)).findAll();
    }
}