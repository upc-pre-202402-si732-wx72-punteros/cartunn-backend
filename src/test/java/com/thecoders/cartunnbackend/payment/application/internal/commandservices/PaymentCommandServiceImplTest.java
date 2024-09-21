package com.thecoders.cartunnbackend.payment.application.internal.commandservices;

import com.thecoders.cartunnbackend.payment.domain.model.aggregates.Payment;
import com.thecoders.cartunnbackend.payment.domain.model.commands.CreatePaymentCommand;
import com.thecoders.cartunnbackend.payment.domain.model.commands.DeletePaymentCommand;
import com.thecoders.cartunnbackend.payment.domain.model.commands.UpdatePaymentCommand;
import com.thecoders.cartunnbackend.payment.infrastructure.persistence.jpa.repositories.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentCommandServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentCommandServiceImpl paymentCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleCreatePaymentCommand() {
        CreatePaymentCommand command = new CreatePaymentCommand("1234567890123456", "12/23", "John Doe", "123", "credit");
        Payment payment = new Payment(command);
        payment.setId(1L);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Long paymentId = paymentCommandService.handle(command);

        assertNotNull(paymentId);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void handleUpdatePaymentCommand() {
        UpdatePaymentCommand command = new UpdatePaymentCommand(1L, "1234567890123456", "12/23", "John Doe", "123", "credit");
        Payment payment = new Payment("1234567890123456", "12/23", "John Doe", "123", "credit");
        when(paymentRepository.findById(command.paymentId())).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Optional<Payment> updatedPayment = paymentCommandService.handle(command);

        assertTrue(updatedPayment.isPresent());
        verify(paymentRepository, times(1)).findById(command.paymentId());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void handleUpdatePaymentCommand_PaymentDoesNotExist() {
        UpdatePaymentCommand command = new UpdatePaymentCommand(1L, "1234567890123456", "12/23", "John Doe", "123", "credit");
        when(paymentRepository.findById(command.paymentId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentCommandService.handle(command);
        });

        assertEquals("Payment does not exist", exception.getMessage());
        verify(paymentRepository, times(1)).findById(command.paymentId());
    }

    @Test
    void handleDeletePaymentCommand() {
        DeletePaymentCommand command = new DeletePaymentCommand(1L);
        when(paymentRepository.existsById(command.PaymentId())).thenReturn(true);

        paymentCommandService.handle(command);

        verify(paymentRepository, times(1)).existsById(command.PaymentId());
        verify(paymentRepository, times(1)).deleteById(command.PaymentId());
    }

    @Test
    void handleDeletePaymentCommand_PaymentDoesNotExist() {
        DeletePaymentCommand command = new DeletePaymentCommand(1L);
        when(paymentRepository.existsById(command.PaymentId())).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentCommandService.handle(command);
        });

        assertEquals("Cart does not exist", exception.getMessage());
        verify(paymentRepository, times(1)).existsById(command.PaymentId());
    }
}