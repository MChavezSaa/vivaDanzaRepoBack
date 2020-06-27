package com.example.vivadanza.Models.DAO;

import com.example.vivadanza.Models.Entity.Usuario;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {

    @Query("select u from Usuario u where u.username= ?1")
    public Usuario findByUsername(String username);


    @Modifying
    @Query(value = "insert into usuario_roles (usuario_id,role_id) VALUES (:usuario_id,:role_id)", nativeQuery = true)
    @Transactional
    void saveUsuario_Roles(@Param("usuario_id") Long usuario_id, @Param("role_id") Long id);

}
