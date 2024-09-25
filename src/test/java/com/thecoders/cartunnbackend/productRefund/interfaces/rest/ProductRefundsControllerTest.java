package com.thecoders.cartunnbackend.productRefunds.interfaces.rest;

import com.thecoders.cartunnbackend.productRefunds.domain.model.aggregates.ProductRefund;
import com.thecoders.cartunnbackend.productRefunds.domain.model.queries.GetAllProductRefundsQuery;
import com.thecoders.cartunnbackend.productRefunds.domain.model.queries.GetProductRefundByIdQuery;
import com.thecoders.cartunnbackend.productRefunds.domain.services.ProductRefundCommandService;
import com.thecoders.cartunnbackend.productRefunds.domain.services.ProductRefundQueryService;
import com.thecoders.cartunnbackend.productRefunds.interfaces.rest.resources.CreateProductRefundResource;
import com.thecoders.cartunnbackend.productRefunds.interfaces.rest.resources.ProductRefundResource;
import com.thecoders.cartunnbackend.productRefunds.interfaces.rest.resources.UpdateProductRefundResource;
import com.thecoders.cartunnbackend.productRefunds.interfaces.rest.transform.ProductRefundResourceFromEntityAssembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductRefundsController.class)
class ProductRefundsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ProductRefundCommandService productRefundCommandService;

    @Mock
    private ProductRefundQueryService productRefundQueryService;

    @InjectMocks
    private ProductRefundsController productRefundsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProductRefund_GivenValidRequest_ShouldReturnCreatedProductRefund() throws Exception {
        // Arrange
        CreateProductRefundResource createResource = new CreateProductRefundResource("Refund 1", "Description", "NEW");
        ProductRefund productRefund = new ProductRefund("Refund 1", "Description", "NEW");
        //productRefund.setId(1L);

        //when(productRefundCommandService.handle(any())).thenReturn(1L);
        when(productRefundQueryService.handle(any(GetProductRefundByIdQuery.class))).thenReturn(Optional.of(productRefund));
        when(ProductRefundResourceFromEntityAssembler.toResourceFromEntity(any(ProductRefund.class)))
                .thenReturn(new ProductRefundResource(1L, "Refund 1", "Description", "NEW"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/product-refund")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"title\": \"Refund 1\", \"description\": \"Description\", \"status\": \"NEW\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Refund 1"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.status").value("NEW"));
    }

    @Test
    void createProductRefund_GivenBadRequest_ShouldReturnBadRequest() throws Exception {
        // Arrange
        CreateProductRefundResource createResource = new CreateProductRefundResource("Refund 1", "Description", "NEW");

        //when(productRefundCommandService.handle(any())).thenReturn(0L); // Indica que hubo un error

        // Act & Assert
        mockMvc.perform(post("/api/v1/product-refund")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"title\": \"Refund 1\", \"description\": \"Description\", \"status\": \"NEW\" }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getProductRefund_GivenExistingProductRefund_ShouldReturnProductRefund() throws Exception {
        // Arrange
        Long productRefundId = 1L;
        ProductRefund productRefund = new ProductRefund("Refund 1", "Description", "NEW");
        //productRefund.setId(productRefundId);

        when(productRefundQueryService.handle(any(GetProductRefundByIdQuery.class))).thenReturn(Optional.of(productRefund));
        when(ProductRefundResourceFromEntityAssembler.toResourceFromEntity(any(ProductRefund.class)))
                .thenReturn(new ProductRefundResource(1L, "Refund 1", "Description", "NEW"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/product-refund/{productRefundId}", productRefundId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Refund 1"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.status").value("NEW"));
    }

    @Test
    void getProductRefund_GivenNonExistingProductRefund_ShouldReturnNotFound() throws Exception {
        // Arrange
        Long productRefundId = 1L;

        when(productRefundQueryService.handle(any(GetProductRefundByIdQuery.class))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/product-refund/{productRefundId}", productRefundId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllProductRefunds_ShouldReturnListOfProductRefunds() throws Exception {
        // Arrange
        ProductRefund productRefund1 = new ProductRefund("Refund 1", "Description 1", "NEW");
        ProductRefund productRefund2 = new ProductRefund("Refund 2", "Description 2", "PROCESSED");

        when(productRefundQueryService.handle(any(GetAllProductRefundsQuery.class))).thenReturn(List.of(productRefund1, productRefund2));
        when(ProductRefundResourceFromEntityAssembler.toResourceFromEntity(any(ProductRefund.class)))
                .thenReturn(new ProductRefundResource(1L, "Refund 1", "Description 1", "NEW"))
                .thenReturn(new ProductRefundResource(2L, "Refund 2", "Description 2", "PROCESSED"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/product-refund")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Refund 1"))
                .andExpect(jsonPath("$[0].description").value("Description 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Refund 2"))
                .andExpect(jsonPath("$[1].description").value("Description 2"));
    }

    @Test
    void updateProductRefund_GivenValidRequest_ShouldReturnUpdatedProductRefund() throws Exception {
        // Arrange
        Long productRefundId = 1L;
        UpdateProductRefundResource updateResource = new UpdateProductRefundResource("Refund 1 Updated", "Description Updated", "PROCESSED");
        ProductRefund updatedProductRefund = new ProductRefund("Refund 1 Updated", "Description Updated", "PROCESSED");
        //updatedProductRefund.setId(productRefundId);

        //when(productRefundCommandService.handle(any())).thenReturn(Optional.of(updatedProductRefund));
        when(ProductRefundResourceFromEntityAssembler.toResourceFromEntity(any(ProductRefund.class)))
                .thenReturn(new ProductRefundResource(productRefundId, "Refund 1 Updated", "Description Updated", "PROCESSED"));

        // Act & Assert
        mockMvc.perform(put("/api/v1/product-refund/{productRefundId}", productRefundId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"title\": \"Refund 1 Updated\", \"description\": \"Description Updated\", \"status\": \"PROCESSED\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Refund 1 Updated"))
                .andExpect(jsonPath("$.description").value("Description Updated"))
                .andExpect(jsonPath("$.status").value("PROCESSED"));
    }
}
