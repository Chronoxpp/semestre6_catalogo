package com.tads20262.catalago.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService
{
    @Autowired
    private ProductRepository repository;

    @Transactional
    public List<Product> findAll()
    {
        return repository.findAll();
    }
}
