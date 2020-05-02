package com.example.vivadanza.Models.DAO;

import com.example.vivadanza.Models.Entity.Foto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFotoDao extends JpaRepository<Foto, Long>{
}
