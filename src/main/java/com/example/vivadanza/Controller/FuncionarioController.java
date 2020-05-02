package com.example.vivadanza.Controller;

import com.example.vivadanza.Models.Entity.Funcionario;
import com.example.vivadanza.Models.Services.IMPL.FuncionarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@CrossOrigin(origins = "*")
@RestController
public class FuncionarioController {
    @Autowired
    private FuncionarioServiceImpl funcionarioService;


//    @Secured({"ROLE_ADMIN","ROLE_CLIENT"})
    @RequestMapping(value = "/listFuncionario", method = RequestMethod.GET)
    public List<Funcionario> findAll(){
        List<Funcionario> all = funcionarioService.findAll();
        return all;
    }

    @PostMapping(value = "/saveFuncionario")
    @ResponseStatus(value = CREATED)
    public ResponseEntity<?> create(@RequestBody Funcionario funcionario){
        funcionario.setEstado_funcionario(true);
        Funcionario funcionario1= null;
        Map<String,Object> response =new HashMap<String, Object>();

        try {
            funcionario1=funcionarioService.save(funcionario);

        }catch(DataAccessException e) {
            response.put("mensaje","Error al realizar el insert en la base de datos");
            response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response, INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El funcionario ha sido creado con éxito!");
        response.put("Funcionario",funcionario1);

        return new ResponseEntity<Map<String,Object>>(response, OK);


    }

}
/*
//guardado de imagen del funcionario => cambiar profesional x funcionario
@Secured("ROLE_ADMIN")
@PostMapping(value= "/saveimagen")
public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo ,@RequestParam("id") Long id ){
    try {
        Profesional profesional = profesionalService.findOne(id);
        System.out.print(profesional.toString());
        if(!archivo.isEmpty()) {
            String nombreArchivo = UUID.randomUUID().toString()+"_"+ archivo.getOriginalFilename().replace(" ","");
            Path rutaArchivo = Paths.get("home/alvaro.castillo1501/uploads").resolve(nombreArchivo).toAbsolutePath();
            Files.copy(archivo.getInputStream(), rutaArchivo);
            String nombreFotoAnterior = profesional.getFoto();
            if(nombreFotoAnterior != null &&  nombreFotoAnterior.length() >0) {
                Path rutaFotoAnterior = Paths.get("home/alvaro.castillo1501/uploads").resolve(nombreFotoAnterior).toAbsolutePath();
                File archivoFotoAnterior = rutaFotoAnterior.toFile();
                if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
                    archivoFotoAnterior.delete();
                }
            }
            profesional.setFoto(nombreArchivo);
            profesionalService.save(profesional);
        }
    }catch(DataAccessException e) {
        return new ResponseEntity<Profesional>(HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (IOException e) {
        e.printStackTrace();
    }

    return new ResponseEntity<Profesional>(HttpStatus.OK);
}
* */