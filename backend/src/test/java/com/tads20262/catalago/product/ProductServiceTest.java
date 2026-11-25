package com.tads20262.catalago.product;

import com.tads20262.catalago.category.CategoryRepository;
import com.tads20262.catalago.service_exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
}
