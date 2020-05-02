package com.example.vivadanza.Models.Services.Interface;

import com.example.vivadanza.Models.Entity.Album;
import com.example.vivadanza.Models.Entity.Funcionario;

import java.util.List;
import java.util.Optional;

public interface IAlbumService {
    List<Album> findAll();
    Album save(Album album);
    Optional<Album> findOne(long id);
    void delete(Album album);
    void deletebyID(long id);
}
