package com.example.vivadanza.Controller;

import com.example.vivadanza.Models.Entity.Album;
import com.example.vivadanza.Models.Entity.Funcionario;
import com.example.vivadanza.Models.Services.IMPL.AlbumServiceIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@CrossOrigin(origins = "*")
@RestController
public class AlbumController {
    @Autowired
    private AlbumServiceIMPL albumServiceIMPL;


    @RequestMapping(value = "/ListAlbumes", method = RequestMethod.GET)
    public List<Album> findAll() {
        List<Album> all = albumServiceIMPL.findAll();
        return all;
    }

    @Secured({"ROLE_ADMIN","ROLE_EMP"})
    @PostMapping(value = "/saveAlbum")
    @ResponseStatus(value = CREATED)
    public ResponseEntity<?> create(@RequestBody Album album) {
        album.setEstado(true);
        Album album1 = null;
        Map<String, Object> response = new HashMap<String, Object>();

        try {

            album1 = albumServiceIMPL.save(album);

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El Album ha sido creado con éxito!");
        response.put("Album", album1);

        return new ResponseEntity<Map<String, Object>>(response, OK);
    }

    @Secured({"ROLE_ADMIN","ROLE_EMP"})
    @GetMapping("/AlbumID/{id}")
    public ResponseEntity<?> findOne(@PathVariable Long id) {
        Album al = null;
        Map<String, Object> response = new HashMap<String, Object>();

        try {
            al = albumServiceIMPL.findOne(id).get();
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (al == null) {
            response.put("mensaje", "El album con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(al, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping(value = "/updateAlbum/{id}")
    public ResponseEntity<?> update(@RequestBody Album album, @PathVariable Long id) {
        Album album1 = albumServiceIMPL.findOne(id).get();
        Album album2 = null;
        Map<String, Object> response = new HashMap<String, Object>();
        if (album1 == null) {
            response.put("mensaje", "No se pudo editar, el album con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        try {
            album1.setEstado(album.isEstado());
            album1.setNombre(album.getNombre());
            album2 = albumServiceIMPL.save(album1);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar la album en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "La album ha sido actualizado con éxito!");
        response.put("album", album2);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}
