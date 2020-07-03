package com.example.vivadanza.Models.Services.IMPL;

import com.example.vivadanza.Models.DAO.IContactoDao;
import com.example.vivadanza.Models.Entity.Contacto;
import com.example.vivadanza.Models.Services.Interface.IContactoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ContactoServiceIMPL implements IContactoService {
   @Autowired
   private IContactoDao contactoDao;

    @Transactional
    @Override
    public Contacto save(Contacto contacto) {
        return contactoDao.save(contacto);
    }


    @Transactional
    @Override
    public Contacto findbyId(Long id) {
        return contactoDao.findById(id).get();
    }
}
