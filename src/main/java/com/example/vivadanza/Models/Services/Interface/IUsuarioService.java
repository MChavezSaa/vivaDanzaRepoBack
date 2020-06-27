package com.example.vivadanza.Models.Services.Interface;

import com.example.vivadanza.Models.Entity.Usuario;

public interface IUsuarioService{

    Usuario findByUsername(String username);
    Usuario save(Usuario us);
    Usuario findById(Long id);
    void deleteById(Long id);
    void saveUsuario_Roles(Long id_User, Long id_Rol);

}
