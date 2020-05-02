package com.example.vivadanza.Models.Services.IMPL;

import com.example.vivadanza.Models.DAO.IAlbumDao;
import com.example.vivadanza.Models.Entity.Album;
import com.example.vivadanza.Models.Services.Interface.IAlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class AlbumServiceIMPL implements IAlbumService {

    @Autowired
    private IAlbumDao albumDao;

    @Transactional
    public List<Album> findAll() {
        return albumDao.findAll();
    }

    @Transactional
    public Album save(Album album) {
        return albumDao.save(album);
    }

    @Transactional
    public Optional<Album> findOne(long id) {
        return albumDao.findById(id);
    }

    @Transactional
    public void delete(Album album) {
        album.setEstado(false);
        albumDao.save(album);
    }

    @Transactional
    public void deletebyID(long id) {
        Album album= albumDao.findById(id).get();
        album.setEstado(false);
        albumDao.save(album);
    }
}
