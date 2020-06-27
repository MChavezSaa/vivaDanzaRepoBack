package com.example.vivadanza.Controller;

import com.example.vivadanza.Models.Entity.Funcionario;
import com.example.vivadanza.Models.Entity.Role;
import com.example.vivadanza.Models.Entity.Usuario;
import com.example.vivadanza.Models.Services.IMPL.FuncionarioServiceImpl;
import com.example.vivadanza.Models.Services.IMPL.RoleSevice;
import com.example.vivadanza.Models.Services.IMPL.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

import static org.springframework.http.HttpStatus.*;

@CrossOrigin(origins = "*")
@RestController
public class FuncionarioController {
    @Autowired
    private FuncionarioServiceImpl funcionarioService;
    @Autowired
    private RoleSevice roleService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    List<Role> rolesList;


//    @Secured({"ROLE_ADMIN","ROLE_CLIENT"})
    @RequestMapping(value = "/listFuncionario", method = RequestMethod.GET)
    public List<Funcionario> findAll(){
        List<Funcionario> all = funcionarioService.findAll();
        return all;
    }

    @Secured({"ROLE_ADMIN","ROLE_EMP"})
    @RequestMapping(value = "/getFunByRut/{rut}", method = RequestMethod.GET)
    public Funcionario findOne(@PathVariable String rut){
        Funcionario fun = funcionarioService.findByRut(rut);
        return fun;
    }

    @Secured({"ROLE_ADMIN","ROLE_EMP"})
    @PostMapping(value = "/saveFuncionario")
    @ResponseStatus(value = CREATED)
    public ResponseEntity<?> create(@RequestBody Funcionario funcionario){
        funcionario.setEstado_funcionario(true);
        Funcionario funcionario1= null;
        Map<String,Object> response =new HashMap<String, Object>();

        rolesList = roleService.findAll();
        try {
            funcionario1=funcionarioService.save(funcionario);
            crearUsuario(funcionario, rolesList);

        }catch(DataAccessException e) {
            response.put("mensaje","Error al realizar el insert en la base de datos");
            response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response, INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El funcionario ha sido creado con éxito!");
        response.put("Funcionario",funcionario1);

        return new ResponseEntity<Map<String,Object>>(response, OK);


    }

    @Secured({"ROLE_ADMIN","ROLE_EMP"})
    @PutMapping(value = "/updateFuncionario/{id}")
    @ResponseStatus(value = CREATED)
    public ResponseEntity<?> update(@RequestBody Funcionario funcionario , @PathVariable Long id){
        Funcionario funcionarioActual = funcionarioService.findOne(id).get();
        Funcionario funcionarioUpdated = null;

        Map<String, Object> response = new HashMap<String, Object>();

        if (funcionarioActual == null) {
            response.put("mensaje", "No se pudo editar, el funcionario con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        try {
            funcionarioActual.setRut(funcionario.getRut());
            funcionarioActual.setNombres(funcionario.getNombres());
            funcionarioActual.setApellidos(funcionario.getApellidos());
            funcionarioActual.setCargo(funcionario.getCargo());


            funcionarioUpdated = funcionarioService.save(funcionarioActual);

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar el funcionario en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El funcionario ha sido actualizado con éxito!");
        response.put("funcionario", funcionarioUpdated);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/DeleteFuncionario/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Funcionario funcionarioBuscado = funcionarioService.findOne(id).get();
        System.out.println(funcionarioBuscado.toString());
        Usuario user = usuarioService.findByUsername(funcionarioBuscado.getRut());
        System.out.println(user.toString());
        Funcionario funcionario2 = null;

        Map<String, Object> response = new HashMap<String, Object>();
        try {

            funcionarioBuscado.setEstado_funcionario(false);
            funcionario2 = funcionarioService.save(funcionarioBuscado);
            usuarioService.deleteById(user.getId_Usuario());



        } catch (DataAccessException e) {
            response.put("mensaje", "Error al eliminar el funcionario de la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El funcionario fue eliminado con éxito!");
        response.put("Funcionario: ", funcionario2);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

    }

    @Secured({"ROLE_ADMIN","ROLE_EMP"})
    @RequestMapping(value = "/darAltaFunc/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> darAlta(@PathVariable Long id) {
        Funcionario funcionarioActual = funcionarioService.findOne(id).get();
        funcionarioActual.setEstado_funcionario(true);
        Funcionario funcionarioUpdated = null;
        rolesList=null;
        rolesList=  roleService.findAll();
        Usuario user = new Usuario();

        Map<String, Object> response = new HashMap<String, Object>();
        if (funcionarioActual == null) {
            response.put("mensaje", "No se pudo dar de alta, el funcionario con el ID: ".concat(id.toString()
                    .concat(" no existe en la base de datos")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        try {
            funcionarioUpdated = funcionarioService.save(funcionarioActual);
            user.setEnable(true);
            user.setNombre(funcionarioActual.getNombres());
            String pass1 = "12345";
            String psEncoder2 = passwordEncoder.encode(pass1);
            user.setPassword(psEncoder2);
            user.setUsername(funcionarioActual.getRut());

            if(funcionarioActual.getCargo().equalsIgnoreCase("Administrativo")){
                usuarioService.save(user);
                usuarioService.saveUsuario_Roles(user.getId_Usuario(),
                        rolesList.get(0).getId_Role());
            }else{
                usuarioService.save(user);
                usuarioService.saveUsuario_Roles(user.getId_Usuario(),
                        rolesList.get(1).getId_Role());
            }



        } catch (DataAccessException e) {
            response.put("mensaje", "Error al dar de alta el funcionario en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El funcionario ha sido dado de alta con éxito!");
        response.put("funcionario", funcionarioUpdated);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

    }


    /*subir foto*/
    @Secured({"ROLE_ADMIN","ROLE_EMP"})
    @PostMapping(value = "/SubirImagenFunc")
    public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id) {
        Map<String, Object> response = new HashMap<String, Object>();
        Funcionario funcionario = funcionarioService.findOne(id).get();

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

            String nombreFotoAnterior = funcionario.getFoto();
            if(nombreFotoAnterior != null && nombreFotoAnterior.length() > 0){
                Path rutaFotoAnterior = Paths.get("Uploads").resolve(nombreFotoAnterior).toAbsolutePath();
                File archivoFotoAnterior = rutaFotoAnterior.toFile();
                if (archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()){
                    archivoFotoAnterior.delete();
                }

            }

            funcionario.setFoto(nombreArchivo);
            funcionarioService.save(funcionario);

            response.put("mensaje", "La imagen ha sido creado con éxito!");
            response.put("Imagen", nombreArchivo);
        }
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN","ROLE_EMP"})
    @GetMapping("/findFuncionario/{id}")
    public ResponseEntity<?> findOne(@PathVariable Long id) {
       Funcionario fun =null;
        Map<String, Object> response = new HashMap<String, Object>();

        try {
            fun = funcionarioService.findOne(id).get();
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (fun == null) {
            response.put("mensaje", "El funcionario con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(fun, HttpStatus.OK);
    }

    private void crearUsuario(@RequestBody Funcionario funcionario, List<Role> rolesList1) {
        String expression = funcionario.getCargo();
        if (expression.equalsIgnoreCase("Administrativo")) {
            Usuario us2 = new Usuario();
            us2.setUsername(funcionario.getRut());
            String pass1 = "12345";
            String psEncoder2 = passwordEncoder.encode(pass1);
            us2.setPassword(psEncoder2);
            us2.setEnable(true);
            us2.setNombre(funcionario.getNombres());
            usuarioService.save(us2);
            usuarioService.saveUsuario_Roles(us2.getId_Usuario(),
                    rolesList1.get(0).getId_Role());
        }
        else{
            Usuario us3 = new Usuario();
            us3.setUsername(funcionario.getRut());
            String pass1 = "12345";
            String psEncoder2 = passwordEncoder.encode(pass1);
            us3.setPassword(psEncoder2);
            us3.setEnable(true);
            us3.setNombre(funcionario.getNombres());
            usuarioService.save(us3);
            usuarioService.saveUsuario_Roles(us3.getId_Usuario(),
                    rolesList1.get(1).getId_Role());
        }
    }
}