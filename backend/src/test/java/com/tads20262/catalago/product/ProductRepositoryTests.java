package com.tads20262.catalago.product;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ProductRepositoryTests {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void findById_returnsNonEmptyOptional_whenIdExists() {
        Product product = new Product();
        product.setName("Teclado");

        Product savedProduct = repository.saveAndFlush(product);

        Long existingId = savedProduct.getId();

        entityManager.clear();

        Optional<Product> actualProduct = repository.findById(existingId);

        assertTrue(actualProduct.isPresent());
        assertEquals(existingId, actualProduct.orElseThrow().getId());
        assertEquals("Teclado", actualProduct.orElseThrow().getName());
    }

    @Test
    void findById_returnsEmptyOptional_whenIdDoesNotExist() {
        Optional<Product> actualProduct = repository.findById(-1L);

        assertTrue(actualProduct.isEmpty());
    }
}
