package com.example.vivadanza.Controller;

import com.example.vivadanza.Models.Entity.Album;
import com.example.vivadanza.Models.Entity.Carousel;
import com.example.vivadanza.Models.Entity.Foto;
import com.example.vivadanza.Models.Services.IMPL.CarouselServiceIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@CrossOrigin(origins = "*")
@RestController
public class CarouselController {
    @Autowired
    private CarouselServiceIMPL carouselServiceIMPL;

    @RequestMapping(value = "/carousel", method = RequestMethod.GET)
    public List<Carousel> findAll() {
        List<Carousel> all = carouselServiceIMPL.findAll();
        return all;
    }
    //subir foto que no existe a carousel
    //@Secured({"ROLE_ADMIN","ROLE_ESTETI"})
    @PostMapping(value = "/saveImagenNueva")
    public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo) {
        Map<String, Object> response = new HashMap<String, Object>();
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
            Carousel carousel = new Carousel();
            carousel.setEstado(true);
            carousel.setFile(nombreArchivo);
            carouselServiceIMPL.save(carousel);

            response.put("mensaje", "La imagen de carrusel ha sido creada con éxito!");
            response.put("Imagen de carrusel", carousel);
        }
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/saveImagenExistente")
    public ResponseEntity<?> uploadExistente(@RequestBody Carousel carousel) {
        Map<String, Object> response = new HashMap<String, Object>();
        carousel.setEstado(true);
        Carousel carousel1 = carouselServiceIMPL.save(carousel);
        response.put("mensaje", "La imagen del carrusel ha sido registrada con éxito!");
        response.put("Imagen de carrusel", carousel1);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @DeleteMapping("/desacCar/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<String, Object>();
        try {
            Carousel fotoAEliminar = carouselServiceIMPL.findbyId(id);
            carouselServiceIMPL.delete(fotoAEliminar);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al desactivar la foto de la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "La foto fue desactivada con éxito!");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

}
