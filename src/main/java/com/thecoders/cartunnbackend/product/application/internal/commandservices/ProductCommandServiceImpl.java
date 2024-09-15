package com.thecoders.cartunnbackend.product.application.internal.commandservices;
import com.thecoders.cartunnbackend.product.domain.model.aggregates.Product;
import com.thecoders.cartunnbackend.product.domain.model.commands.CreateProductCommand;
import com.thecoders.cartunnbackend.product.domain.model.commands.DeleteProductCommand;
import com.thecoders.cartunnbackend.product.domain.model.commands.UpdateProductCommand;
import com.thecoders.cartunnbackend.product.domain.services.ProductCommandService;
import com.thecoders.cartunnbackend.product.infrastructure.persitence.jpa.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductCommandServiceImpl implements ProductCommandService {
    private final ProductRepository productRepository;

    public ProductCommandServiceImpl(ProductRepository productRepository) {this.productRepository = productRepository;}

    @Override
    public Long handle(CreateProductCommand command) {
        if (productRepository.existsByTitle(command.title())) {
            throw new IllegalArgumentException("Product with same title already exists");
        }
        var product = new Product(command);
        try {
            productRepository.save(product);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while saving product: " + e.getMessage());
        }
        return product.getId();
    }
    @Override
    public Optional<Product> handle(UpdateProductCommand command) {
        if (productRepository.existsByTitleAndIdIsNot(command.title(), command.id()))
            throw new IllegalArgumentException("Product with same title already exists");
        var result = productRepository.findById(command.id());
        if (result.isEmpty()) throw new IllegalArgumentException("Product does not exist");
        var productToUpdate = result.get();
        try {
            var updatedProduct = productRepository.save(productToUpdate.updateInformation(command.title(), command.description(), command.image(), command.price()));
            return Optional.of(updatedProduct);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating product: " + e.getMessage());
        }
    }
    @Override
    public void handle(DeleteProductCommand command) {
        if (!productRepository.existsById(command.productId())) {
            throw new IllegalArgumentException("Product does not exist");
        }
        try {
            productRepository.deleteById(command.productId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting product: " + e.getMessage());
        }
    }


}
