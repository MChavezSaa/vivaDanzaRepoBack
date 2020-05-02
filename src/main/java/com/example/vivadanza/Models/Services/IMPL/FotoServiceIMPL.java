package com.example.vivadanza.Models.Services.IMPL;

import com.example.vivadanza.Models.DAO.IFotoDao;
import com.example.vivadanza.Models.Entity.Foto;
import com.example.vivadanza.Models.Services.Interface.IFotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class FotoServiceIMPL implements IFotoService {
    @Autowired
    private IFotoDao fotoDao;

    @Transactional
    public List<Foto> findAll() {
        return fotoDao.findAll();
    }

    @Transactional
    public Foto save(Foto foto) {
        return fotoDao.save(foto);
    }

    @Transactional
    public void delete(Foto foto) {
        fotoDao.delete(foto);
    }

    @Override
    public Foto findbyId(Long id) {
        return fotoDao.findById(id).get();
    }


}
