package com.thecoders.cartunnbackend.payment.application.internal.queryservices;

import com.thecoders.cartunnbackend.payment.domain.model.aggregates.Payment;
import com.thecoders.cartunnbackend.payment.domain.model.queries.GetAllPaymentsQuery;
import com.thecoders.cartunnbackend.payment.domain.model.queries.GetPaymentByIdQuery;
import com.thecoders.cartunnbackend.payment.domain.services.PaymentQueryService;
import com.thecoders.cartunnbackend.payment.infrastructure.persistence.jpa.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentQueryServiceImpl implements PaymentQueryService {
    private final PaymentRepository paymentRepository;

    public PaymentQueryServiceImpl(PaymentRepository paymentRepository){
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Optional<Payment> handle(GetPaymentByIdQuery query){
        return paymentRepository.findById(query.PaymentId());
    }

    @Override
    public List<Payment> handle(GetAllPaymentsQuery query){
        return paymentRepository.findAll();
    }
}
