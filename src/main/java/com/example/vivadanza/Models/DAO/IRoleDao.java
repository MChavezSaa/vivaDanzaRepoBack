package com.example.vivadanza.Models.DAO;
import com.example.vivadanza.Models.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleDao extends JpaRepository<Role, Long> {
}
