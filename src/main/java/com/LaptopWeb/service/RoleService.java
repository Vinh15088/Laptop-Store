package com.LaptopWeb.service;

import com.LaptopWeb.entity.Role;
import com.LaptopWeb.entity.User;
import com.LaptopWeb.exception.AppException;
import com.LaptopWeb.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRopository;

    public List<Role> getAll() {
        return roleRopository.findAll();
    }

    public Role getRole(String name) throws Exception {
        return roleRopository.findById(name).orElseThrow(() -> new Exception("Not found"));
    }

}
