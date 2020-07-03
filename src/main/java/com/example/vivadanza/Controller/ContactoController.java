package com.example.vivadanza.Controller;

import com.example.vivadanza.Models.Entity.Album;
import com.example.vivadanza.Models.Entity.Contacto;
import com.example.vivadanza.Models.Services.IMPL.ContactoServiceIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@CrossOrigin(origins = "*")
@RestController
public class ContactoController {

    @Autowired
    private ContactoServiceIMPL contactoServiceIMPL;

    @Secured("ROLE_ADMIN")
    @PutMapping(value = "/saveContacto")
    @ResponseStatus(value = CREATED)
    public ResponseEntity<?> create(@RequestBody Contacto contacto) {

        Contacto contactoResponse = null;
        Map<String, Object> response = new HashMap<String, Object>();

        try {

            contactoResponse = contactoServiceIMPL.save(contacto);

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause()
                    .getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El contacto ha sido actualizado con éxito!");
        response.put("Contacto", contactoResponse);

        return new ResponseEntity<Map<String, Object>>(response, OK);
    }

    @GetMapping("/getContacto")
    public ResponseEntity<?> findOne() {
        Contacto contacto = null;
        Map<String, Object> response = new HashMap<String, Object>();

        try {
            contacto = contactoServiceIMPL.findbyId(Long.parseLong("1"));
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ")
                    .concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (contacto == null) {
            response.put("mensaje", "El contacto con el ID: "
                    .concat("1".toString().concat(" no existe en la base de datos")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(contacto, HttpStatus.OK);
    }

}
