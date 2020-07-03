package com.example.vivadanza.Models.DAO;

import com.example.vivadanza.Models.Entity.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IContactoDao extends JpaRepository<Contacto, Long> {
}
