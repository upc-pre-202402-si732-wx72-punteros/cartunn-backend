package com.thecoders.cartunnbackend.payment.domain.model.aggregates;


import com.thecoders.cartunnbackend.payment.domain.model.commands.CreatePaymentCommand;
import com.thecoders.cartunnbackend.product.domain.model.aggregates.Product;
import com.thecoders.cartunnbackend.productRefunds.domain.model.aggregates.ProductRefund;
import com.thecoders.cartunnbackend.productRefunds.domain.model.commands.CreateProductRefundCommand;
import com.thecoders.cartunnbackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;

@Getter
@Entity
public class Payment  extends AuditableAbstractAggregateRoot<Payment> {
    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(name = "expiration_date", nullable = false)
    private String expirationDate;

    @Column(name = "card_holder", nullable = false)
    private String cardHolder;

    @Column(name = "cvc", nullable = false)
    private String cvc;

    @Column(name = "method_pay", nullable = false)
    private String methodPay;


    public Payment(){
        this.cardNumber = Strings.EMPTY;
        this.expirationDate = Strings.EMPTY;
        this.cardHolder = Strings.EMPTY;
        this.cvc = Strings.EMPTY;
        this.methodPay = Strings.EMPTY;
    }

    public Payment(String cardNumber,
                   String expirationDate,
                   String cardHolder,
                   String cvc, String methodPay){
        this();
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cardHolder = cardHolder;
        this.cvc = cvc;
        this.methodPay = methodPay;
    }

    public Payment(CreatePaymentCommand command){
        this();
        this.cardNumber = command.cardNumber();
        this.expirationDate = command.expirationDate();
        this.cardHolder = command.cardHolder();
        this.cvc = command.cvc();
        this.methodPay = command.methodPay();
    }

    public Payment updateInformation(String cardNumber,
                                     String expirationDate,
                                     String cardHolder,
                                     String cvc,
                                     String methodPay){
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cardHolder = cardHolder;
        this.cvc = cvc;
        this.methodPay = methodPay;
        return this;
    }

}
