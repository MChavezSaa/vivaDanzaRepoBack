package com.example.vivadanza.Controller;

import com.example.vivadanza.Models.Entity.Usuario;
import com.example.vivadanza.Models.Services.IMPL.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Secured({"ROLE_ADMIN", "ROLE_EMPLEADO"})
    @PutMapping(value = "/cambioPass")
    public ResponseEntity<?> cambiopass(@RequestBody Usuario user) {
        Map<String, Object> response = new HashMap<String, Object>();
        Usuario userBDD = usuarioService.findByUsername(user.getUsername());
        try {
            userBDD.setPassword(passwordEncoder.encode(user.getPassword()));
            usuarioService.save(userBDD);

        } catch (DataAccessException e) {
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

    }
}
