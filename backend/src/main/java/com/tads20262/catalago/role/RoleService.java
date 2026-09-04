package com.tads20262.catalago.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService
{
    @Autowired
    private RoleRepository repository;

    @Transactional
    public List<Role> findAll()
    {
        return repository.findAll();
    }
}
