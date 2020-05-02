package com.example.vivadanza.Models.DAO;

import com.example.vivadanza.Models.Entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFuncionarioDao extends JpaRepository<Funcionario, Long> {

}
