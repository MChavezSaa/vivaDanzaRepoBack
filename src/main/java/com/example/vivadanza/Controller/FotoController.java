package com.example.vivadanza.Controller;

import com.example.vivadanza.Models.Entity.Album;
import com.example.vivadanza.Models.Entity.Foto;
import com.example.vivadanza.Models.Services.IMPL.AlbumServiceIMPL;
import com.example.vivadanza.Models.Services.IMPL.FotoServiceIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@CrossOrigin(origins = "*")
@RestController
public class FotoController {
    @Autowired
    private FotoServiceIMPL fotoServiceIMPL;
    @Autowired
    private AlbumServiceIMPL albumServiceIMPL;


    @RequestMapping(value = "/ListFotos", method = RequestMethod.GET)
    public List<Foto> findAll() {
        List<Foto> all = fotoServiceIMPL.findAll();
        return all;
    }

    @Secured({"ROLE_ADMIN","ROLE_EMP"})
    @RequestMapping(value = "/fotosPorAlbum/{id}", method = RequestMethod.GET)
    public List<Foto> findFotosPorAlbum(@PathVariable Long id) {
        List<Foto> all = fotoServiceIMPL.findAll();
        List<Foto> all2 = new LinkedList<>();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getAlbum().getId_album() == id) {
                all2.add(all.get(i));
            }
        }
        return all2;
    }

    /*ver foto*/
    @GetMapping("uploads/{nombreFoto:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto) {
        Path rutaArchivo = Paths.get("Uploads").resolve(nombreFoto).toAbsolutePath();
        Resource recurso = null;
        try {
            recurso = new UrlResource(rutaArchivo.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (!recurso.exists() && !recurso.isReadable()) {
            throw new RuntimeException("Error no se pudo cargar la imagen" + nombreFoto);
        }
        HttpHeaders cabecera = new HttpHeaders();
        cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"");
        return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);
    }

    /*subir foto*/
    @Secured({"ROLE_ADMIN","ROLE_EMP"})
    @PostMapping(value = "/saveimagen")
    public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id) {
        System.out.println(archivo.getOriginalFilename());
        System.out.println(id.toString());
        Map<String, Object> response = new HashMap<String, Object>();
        Album album = albumServiceIMPL.findOne(id).get();
        if (!archivo.isEmpty()) {
            String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename()
                    .replace("", "");
            Path rutaArchivo = Paths.get("Uploads").resolve(nombreArchivo).toAbsolutePath();
            try {
                Files.copy(archivo.getInputStream(), rutaArchivo);
            } catch (IOException e) {
                response.put("mensaje", "Error al guardar la imagen");
                response.put("error", e.getMessage().concat(": ").concat(e.getMessage()));
                return new ResponseEntity<Map<String, Object>>(response, INTERNAL_SERVER_ERROR);
            }

            Foto foto = new Foto();
            foto.setFile(nombreArchivo);
            foto.setAlbum(album);
            fotoServiceIMPL.save(foto);

            response.put("mensaje", "La imagen ha sido creado con éxito!");
            response.put("Imagen", foto);
        }
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN","ROLE_EMP"})
    @DeleteMapping("/deleteFoto/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<String, Object>();
        try {
            Foto fotoAEliminar = fotoServiceIMPL.findbyId(id);
            Path rutaArchivoABorrar = Paths.get("Uploads").resolve(fotoAEliminar.getFile()).toAbsolutePath();
            File fotoAnterior = rutaArchivoABorrar.toFile();
            if (fotoAnterior.exists() && fotoAnterior.canRead()) {
                fotoAnterior.delete();
            }
            fotoServiceIMPL.delete(fotoAEliminar);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al eliminar la foto de la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La foto fue eliminada con éxito!");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

}
