package com.tads20262.catalago.product;

import com.tads20262.catalago.serviceExceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService
{
    @Autowired
    private ProductRepository repository;

    @Transactional
    public List<ProductDTO> findAll()
    {
        List<Product> list = repository.findAll();

        return list
            .stream()
            .map(ProductDTO::new)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id)
    {
        Optional<Product> obj = repository.findById(id);

        Product entity = obj.orElseThrow(()-> new ResourceNotFoundException("Entity Not found"));

        return new ProductDTO(entity);
    }
}
