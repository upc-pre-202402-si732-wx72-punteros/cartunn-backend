package com.thecoders.cartunnbackend.product.interfaces.rest;

import com.thecoders.cartunnbackend.product.domain.model.aggregates.Product;
import com.thecoders.cartunnbackend.product.domain.model.commands.CreateProductCommand;
import com.thecoders.cartunnbackend.product.domain.model.commands.DeleteProductCommand;
import com.thecoders.cartunnbackend.product.domain.model.commands.UpdateProductCommand;
import com.thecoders.cartunnbackend.product.domain.model.queries.GetAllProductsQuery;
import com.thecoders.cartunnbackend.product.domain.model.queries.GetProductByIdQuery;
import com.thecoders.cartunnbackend.product.domain.services.ProductCommandService;
import com.thecoders.cartunnbackend.product.domain.services.ProductQueryService;
import com.thecoders.cartunnbackend.product.interfaces.rest.resources.CreateProductResource;
import com.thecoders.cartunnbackend.product.interfaces.rest.resources.ProductResource;
import com.thecoders.cartunnbackend.product.interfaces.rest.resources.UpdateProductResource;
import com.thecoders.cartunnbackend.product.interfaces.rest.transform.CreateProductCommandFromResourceAssembler;
import com.thecoders.cartunnbackend.product.interfaces.rest.transform.ProductResourceFromEntityAssembler;
import com.thecoders.cartunnbackend.product.interfaces.rest.transform.UpdateProductCommandFromResourceAssembler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductsController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ProductsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductCommandService productCommandService;

    @MockBean
    private ProductQueryService productQueryService;

    @Test
    void createProduct_GivenValidProduct_ShouldReturnCreatedStatusAndProduct() throws Exception {
        // Arrange
        CreateProductResource createProductResource = new CreateProductResource("Product 1", "Description 1", "Image 1", 99.99);
        CreateProductCommand createProductCommand = new CreateProductCommand("Product 1", "Description 1", "Image 1", 99.99);
        Product product = new Product("Product 1", "Description 1", "Image 1", 99.99);
        ProductResource productResource = new ProductResource(1L, "Product 1", "Description 1", "Image 1", 99.99);

        try (MockedStatic<CreateProductCommandFromResourceAssembler> mockedAssembler = mockStatic(CreateProductCommandFromResourceAssembler.class);
             MockedStatic<ProductResourceFromEntityAssembler> mockedAssembler2 = mockStatic(ProductResourceFromEntityAssembler.class)) {
            mockedAssembler.when(() -> CreateProductCommandFromResourceAssembler.toCommandFromResource(createProductResource)).thenReturn(createProductCommand);
            mockedAssembler2.when(() -> ProductResourceFromEntityAssembler.toResourceFromEntity(product)).thenReturn(productResource);
            when(productCommandService.handle(createProductCommand)).thenReturn(1L);
            when(productQueryService.handle(any(GetProductByIdQuery.class))).thenReturn(Optional.of(product));

            // Act
            ResultActions result = mockMvc.perform(post("/api/v1/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"title\":\"Product 1\",\"description\":\"Description 1\",\"image\":\"Image 1\",\"price\":99.99}"));

            // Assert
            result.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.title").value("Product 1"))
                    .andExpect(jsonPath("$.description").value("Description 1"))
                    .andExpect(jsonPath("$.image").value("Image 1"))
                    .andExpect(jsonPath("$.price").value(99.99));
        }
    }

    @Test
    void getProduct_GivenValidProductId_ShouldReturnOkAndProduct() throws Exception {
        // Arrange
        Product product = new Product(
                "Producto 1",
                "Descripcion del producto 1",
                "imagen1.jpg",
                100.0
        );

        var id = 1L;
        var query = new GetProductByIdQuery(id);

        when(productQueryService.handle(query)).thenReturn(Optional.of(product));

        // Act
        ResultActions resultActions = mockMvc.perform(get("/api/v1/products/{productId}", id)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Producto 1"))
                .andExpect(jsonPath("$.description").value("Descripcion del producto 1"))
                .andExpect(jsonPath("$.image").value("imagen1.jpg"))
                .andExpect(jsonPath("$.price").value(100.0));
    }

    @Test
    void getAllProducts_GivenValidQuery_ShouldReturnOkAndListOfProducts() throws Exception {
        // Arrange
        Product product1 = new Product(
                "Producto 1",
                "Descripcion del producto 1",
                "imagen1.jpg",
                100.0
        );

        Product product2 = new Product(
                "Producto 2",
                "Descripcion del producto 2",
                "imagen2.jpg",
                200.0
        );
        List<Product> products = List.of(product1, product2);

        when(productQueryService.handle(any(GetAllProductsQuery.class))).thenReturn(products);

        // Act
        ResultActions resultActions = mockMvc.perform(get("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Producto 1"))
                .andExpect(jsonPath("$[1].title").value("Producto 2"));

    }

    @Test
    void updateProduct_GivenValidProduct_ShouldReturnUpdatedProduct() throws Exception {
        // Arrange
        Long productId = 1L;
        UpdateProductResource updateProductResource = new UpdateProductResource("Updated Product", "Updated Description", "Updated Image", 149.99);
        UpdateProductCommand updateProductCommand = new UpdateProductCommand(productId, "Updated Product", "Updated Description", "Updated Image", 149.99);
        Product updatedProduct = new Product("Updated Product", "Updated Description", "Updated Image", 149.99);
        ProductResource updatedProductResource = new ProductResource(productId, "Updated Product", "Updated Description", "Updated Image", 149.99);

        try (MockedStatic<UpdateProductCommandFromResourceAssembler> mockedCommandAssembler = mockStatic(UpdateProductCommandFromResourceAssembler.class);
             MockedStatic<ProductResourceFromEntityAssembler> mockedResourceAssembler = mockStatic(ProductResourceFromEntityAssembler.class)) {

            mockedCommandAssembler.when(() -> UpdateProductCommandFromResourceAssembler.toCommandFromResource(productId, updateProductResource))
                    .thenReturn(updateProductCommand);

            when(productCommandService.handle(updateProductCommand)).thenReturn(Optional.of(updatedProduct));

            mockedResourceAssembler.when(() -> ProductResourceFromEntityAssembler.toResourceFromEntity(updatedProduct))
                    .thenReturn(updatedProductResource);

            // Act
            ResultActions result = mockMvc.perform(put("/api/v1/products/{productId}", productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"title\":\"Updated Product\",\"description\":\"Updated Description\",\"image\":\"Updated Image\",\"price\":149.99}"));

            // Assert
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(productId))
                    .andExpect(jsonPath("$.title").value("Updated Product"))
                    .andExpect(jsonPath("$.description").value("Updated Description"))
                    .andExpect(jsonPath("$.image").value("Updated Image"))
                    .andExpect(jsonPath("$.price").value(149.99));
        }
    }


    @Test
    void deleteProduct_GivenValidProductId_ShouldReturnSuccessMessage() throws Exception {
        // Arrange
        Long productId = 1L;
        DeleteProductCommand deleteProductCommand = new DeleteProductCommand(productId);

        doNothing().when(productCommandService).handle(deleteProductCommand);

        // Act
        ResultActions result = mockMvc.perform(delete("/api/v1/products/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(content().string("Product deleted successfully"));
    }

}