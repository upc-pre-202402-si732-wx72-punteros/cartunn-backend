package com.thecoders.cartunnbackend.productRefunds.application.internal.commandservices;

import com.thecoders.cartunnbackend.productRefunds.domain.model.aggregates.ProductRefund;
import com.thecoders.cartunnbackend.productRefunds.domain.model.commands.CreateProductRefundCommand;
import com.thecoders.cartunnbackend.productRefunds.domain.model.commands.UpdateProductRefundCommand;
import com.thecoders.cartunnbackend.productRefunds.domain.services.ProductRefundCommandService;
import com.thecoders.cartunnbackend.productRefunds.infrastructure.jpa.persistence.ProductRefundRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductRefundCommandServiceImpl implements ProductRefundCommandService {
    private final ProductRefundRepository productRefundRepository;

    public ProductRefundCommandServiceImpl(ProductRefundRepository productRefundRepository){
        this.productRefundRepository = productRefundRepository;
    }

    @Override
    public Long handle(CreateProductRefundCommand command) {
        if (productRefundRepository.existsByTitle(command.title())) {
            throw new IllegalArgumentException("Product refund with title " + command.title() + " already exists");
        }
        var productRefund = new ProductRefund(command);
        try {
            productRefundRepository.save(productRefund);
            return productRefund.getId();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while saving product refund: " + e.getMessage());
        }
    }


    @Override
    public Optional<ProductRefund> handle(UpdateProductRefundCommand command){
        if(productRefundRepository.existsByTitleAndIdIsNot(command.title(), command.id())){
            throw new IllegalArgumentException("Profile with same product refund already exists");
        }
        var result = productRefundRepository.findById(command.id());
        if (result.isEmpty()){
            throw new IllegalArgumentException("ProductRefund does not exist");
        }
        var productRefundToUpdated = result.get();
        try {
            productRefundToUpdated.updateInformation(command.title(), command.description(), command.status());
            var updatedProfile = productRefundRepository.save(productRefundToUpdated);
            return Optional.of(updatedProfile);
        } catch (Exception e){
            throw new IllegalArgumentException("Error while updating product refund: " + e.getMessage());
        }
    }
}
