package com.example.vivadanza.Models.DAO;

import com.example.vivadanza.Models.Entity.Funcionario;
import com.example.vivadanza.Models.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IFuncionarioDao extends JpaRepository<Funcionario, Long> {

    @Query("select u from Funcionario  u where u.rut=?1")
    public Funcionario findByRut(String rut);
}
