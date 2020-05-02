package com.example.vivadanza.Models.DAO;

import com.example.vivadanza.Models.Entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAlbumDao extends JpaRepository<Album, Long> {
}
