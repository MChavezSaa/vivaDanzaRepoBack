package com.example.vivadanza.Models.Services.IMPL;

import com.example.vivadanza.Models.DAO.IRoleDao;
import com.example.vivadanza.Models.Entity.Role;
import com.example.vivadanza.Models.Services.Interface.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleSevice implements IRoleService {

    @Autowired
    private IRoleDao roleDao;

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }
}