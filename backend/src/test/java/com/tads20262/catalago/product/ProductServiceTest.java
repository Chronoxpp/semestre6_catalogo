package com.tads20262.catalago.product;

import com.tads20262.catalago.category.CategoryRepository;
import com.tads20262.catalago.service_exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void findById_returnsProductDto_whenProductExists() {
        // Arrange: prepara o produto e o resultado do repositório.
        Product product = new Product();
        product.setId(10L);
        product.setName("Teclado");
        when(productRepository.findById(10L))
                .thenReturn(Optional.of(product));

        // Act: chama o método que estamos testando.
        ProductDTO result = productService.findById(10L);

        // Assert: confere os dados devolvidos e a chamada ao repositório.
        assertEquals(10L, result.getId());
        assertEquals("Teclado", result.getName());
        verify(productRepository).findById(10L);
    }

    @Test
    void findById_throwsResourceNotFound_whenProductDoesNotExist() {
        // Arrange: informa ao mock que o ID não foi encontrado.
        when(productRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act + Assert: a chamada deve lançar a exceção esperada.
        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.findById(99L)
        );
        verify(productRepository).findById(99L);
    }

    @Test
    void update_returnsProductDto_whenProductExists()
    {
        Product product = new Product();
        product.setId(10L);
        product.setName("Teclado antigo");

        ProductDTO request = new ProductDTO();
        request.setName("Teclado atualizado");
        request.setDescription("Teclado mecanico");
        request.setPrice(250.0);
        request.setImgUrl("https://example.com/teclado.png");
        request.setDate(Instant.parse("2026-11-25T12:00:00Z"));

        when(productRepository.getReferenceById(10L)).thenReturn(product);
        when(productRepository.save(product)).thenAnswer(invocation -> invocation.getArgument(0));

        ProductDTO response = productService.update(10L, request);

        assertEquals(10L, response.getId());
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getDescription(), response.getDescription());
        assertEquals(request.getPrice(), response.getPrice());
        assertEquals(request.getImgUrl(), response.getImgUrl());
        assertEquals(request.getDate(), response.getDate());

        verify(productRepository).getReferenceById(10L);
        verify(productRepository).save(product);
    }

    @Test
    void update_throwsResourceNotFound_whenProductDoesNotExist()
    {
        long idInexistente = 99L;

        ProductDTO request = new ProductDTO();
        when(productRepository.getReferenceById(idInexistente))
                .thenThrow(new EntityNotFoundException());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.update(idInexistente, request)
        );

        verify(productRepository).getReferenceById(idInexistente);
        verify(productRepository, never()).save(any(Product.class));
    }
}
