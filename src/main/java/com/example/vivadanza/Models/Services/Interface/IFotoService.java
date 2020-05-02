package com.example.vivadanza.Models.Services.Interface;

import com.example.vivadanza.Models.Entity.Album;
import com.example.vivadanza.Models.Entity.Foto;

import java.util.List;

public interface IFotoService {
    List<Foto> findAll();
    Foto save(Foto foto);
    void delete(Foto foto);
    Foto findbyId(Long id);
}
