package com.tads20262.catalago.product;

import com.tads20262.catalago.service_exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
class ProductResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void insert_returnsCreatedAndProductDto() throws Exception {
        ProductDTO savedProduct = new ProductDTO();
        savedProduct.setId(10L);
        savedProduct.setName("Teclado");
        savedProduct.setDescription("Teclado mecanico");
        savedProduct.setPrice(250.0);
        savedProduct.setImgUrl("https://example.com/teclado.png");
        savedProduct.setDate(Instant.parse("2026-11-25T12:00:00Z"));
        when(productService.insert(any(ProductDTO.class))).thenReturn(savedProduct);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Teclado",
                                    "description": "Teclado mecanico",
                                    "price": 250.0,
                                    "imgUrl": "https://example.com/teclado.png",
                                    "date": "2026-11-25T12:00:00Z",
                                    "categories": []
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/products/10"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value(savedProduct.getName()))
                .andExpect(jsonPath("$.description").value(savedProduct.getDescription()))
                .andExpect(jsonPath("$.price").value(savedProduct.getPrice()))
                .andExpect(jsonPath("$.imgUrl").value(savedProduct.getImgUrl()))
                .andExpect(jsonPath("$.date").value(savedProduct.getDate().toString()))
                .andExpect(jsonPath("$.categories").isEmpty());

        ProductDTO expectedInput = new ProductDTO();
        expectedInput.setName(savedProduct.getName());
        expectedInput.setDescription(savedProduct.getDescription());
        expectedInput.setPrice(savedProduct.getPrice());
        expectedInput.setImgUrl(savedProduct.getImgUrl());
        expectedInput.setDate(savedProduct.getDate());
        verify(productService).insert(expectedInput);
    }

    @Test
    void delete_returnsNoContent_whenIdExists() throws Exception {
        mockMvc.perform(delete("/products/{id}", 10L))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(productService).delete(10L);
    }

    @Test
    void delete_returnsNotFound_whenIdDoesNotExist() throws Exception {
        doThrow(new ResourceNotFoundException("Id not found 99"))
                .when(productService).delete(99L);

        mockMvc.perform(delete("/products/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Id not found 99"))
                .andExpect(jsonPath("$.path").value("/products/99"));

        verify(productService).delete(99L);
    }
}
