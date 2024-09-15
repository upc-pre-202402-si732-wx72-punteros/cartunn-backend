package com.thecoders.cartunnbackend.productRefunds.interfaces.rest;

import com.thecoders.cartunnbackend.productRefunds.domain.model.queries.GetAllProductRefundsQuery;
import com.thecoders.cartunnbackend.productRefunds.domain.model.queries.GetProductRefundByIdQuery;
import com.thecoders.cartunnbackend.productRefunds.domain.services.ProductRefundCommandService;
import com.thecoders.cartunnbackend.productRefunds.domain.services.ProductRefundQueryService;
import com.thecoders.cartunnbackend.productRefunds.interfaces.rest.resources.CreateProductRefundResource;
import com.thecoders.cartunnbackend.productRefunds.interfaces.rest.resources.ProductRefundResource;
import com.thecoders.cartunnbackend.productRefunds.interfaces.rest.resources.UpdateProductRefundResource;
import com.thecoders.cartunnbackend.productRefunds.interfaces.rest.transform.CreateProductRefundCommandFromResourceAssembler;
import com.thecoders.cartunnbackend.productRefunds.interfaces.rest.transform.ProductRefundResourceFromEntityAssembler;
import com.thecoders.cartunnbackend.productRefunds.interfaces.rest.transform.UpdateProductRefundCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "api/v1/product-refund", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Product Refunds ", description = "Product Refunds Management Endpoints")

public class ProductRefundsController {
    private final ProductRefundCommandService productRefundCommandService;
    private final ProductRefundQueryService productRefundQueryService;














    public ProductRefundsController(ProductRefundCommandService productRefundCommandService, ProductRefundQueryService productRefundQueryService) {
        this.productRefundCommandService = productRefundCommandService;
        this.productRefundQueryService = productRefundQueryService;
    }


    @PostMapping
    public ResponseEntity<ProductRefundResource> createProductRefund(@RequestBody CreateProductRefundResource createProductRefundResource) {
        var createProductRefundCommand = CreateProductRefundCommandFromResourceAssembler.toCommandFromResource(createProductRefundResource);
        var productRefundId = productRefundCommandService.handle(createProductRefundCommand);
        if (productRefundId == 0L) return ResponseEntity.badRequest().build();

        var getProductRefundByIdQuery = new GetProductRefundByIdQuery(productRefundId);
        var productRefund = productRefundQueryService.handle(getProductRefundByIdQuery);
        if(productRefund.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        var productRefundResource = ProductRefundResourceFromEntityAssembler.toResourceFromEntity(productRefund.get());
        return new ResponseEntity<>(productRefundResource, HttpStatus.CREATED);
    }

    @GetMapping("/{productRefundId}")
    public ResponseEntity<ProductRefundResource> getProductRefund(@PathVariable Long productRefundId){
        var getProductRefundByIdQuery = new GetProductRefundByIdQuery(productRefundId);
        var productRefund = productRefundQueryService.handle(getProductRefundByIdQuery);
        if(productRefund.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var productRefundResource = ProductRefundResourceFromEntityAssembler.toResourceFromEntity(productRefund.get());
        return ResponseEntity.ok(productRefundResource);
    }

    @GetMapping
    public ResponseEntity<List<ProductRefundResource>> getAllProductRefunds(){
        var getAllProductRefundsQuery = new GetAllProductRefundsQuery();
        var productRefunds = productRefundQueryService.handle(getAllProductRefundsQuery);
        var productRefundsResources = productRefunds.stream().map(ProductRefundResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(productRefundsResources);
    }

    @PutMapping("/{productRefundId}")
    public ResponseEntity<ProductRefundResource> updateProductRefund(@PathVariable Long productRefundId, @RequestBody UpdateProductRefundResource updateProductRefundResource){
        var updateProductRefundCommand = UpdateProductRefundCommandFromResourceAssembler.toCommandFromResource(productRefundId, updateProductRefundResource);
        var updateProductRefund = productRefundCommandService.handle(updateProductRefundCommand);
        if (updateProductRefund.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        var productRefundResource = ProductRefundResourceFromEntityAssembler.toResourceFromEntity(updateProductRefund.get());
        return ResponseEntity.ok(productRefundResource);
    }

}
