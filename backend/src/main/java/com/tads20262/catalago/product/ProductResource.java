package com.tads20262.catalago.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/products")
public class ProductResource
{
    @Autowired
    private ProductService service;

    @GetMapping
    public ResponseEntity<List<Product>> findAll()
    {
        List<Product> list = service.findAll();

        return ResponseEntity.ok(list);
    }
}
