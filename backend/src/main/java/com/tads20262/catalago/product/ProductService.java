package com.tads20262.catalago.product;

import com.tads20262.catalago.category.Category;
import com.tads20262.catalago.category.CategoryDTO;
import com.tads20262.catalago.category.CategoryRepository;
import com.tads20262.catalago.service_exceptions.DatabaseException;
import com.tads20262.catalago.service_exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public Page<ProductDTO> findAllPaged(Pageable pageable)
    {
        Page<Product> list = repository.findAll(pageable);

        return list.map(ProductDTO::new);
    }

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

        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto)
    {
        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);

        return new ProductDTO(entity);
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity)
    {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());

        entity.getCategories().clear();
        for(CategoryDTO categoryDto : dto.getCategories())
        {
            Category category = categoryRepository.getReferenceById(categoryDto.getId());
            entity.getCategories().add(category);
        }
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto)
    {
        try
        {
            Product entity = repository.getReferenceById(id);
            //entity.setName(dto.getName());
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);

            return new ProductDTO(entity);
        }
        catch (EntityNotFoundException e)
        {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    public void delete(Long id)
    {
        try
        {
            Optional<Product> obj = repository.findById(id);

            obj.orElseThrow(()-> new ResourceNotFoundException("Id not found " + id));

            repository.deleteById(id);
        }
        catch(DataIntegrityViolationException e)
        {
            throw new DatabaseException("Integrity violation");
        }
    }
}
