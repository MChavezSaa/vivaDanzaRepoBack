package com.example.vivadanza.Controller;

import com.example.vivadanza.Models.Entity.Email;
import com.example.vivadanza.Models.Entity.Funcionario;
import com.example.vivadanza.Models.Services.IMPL.emailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@CrossOrigin(origins = "*")
@RestController
public class emailController {
    //este correo no debe cambiarse a menos que se cambie el properties
    String CORREOPROPERTIES= "matroxmega@gmail.com";

    //correos de reenvio cambiarlos por lo que entreguen los clientes....
    String CORREOREENVIO1= "mchavezsaa96@gmail.com";
    String CORREOREENVIO2= "mchavezsaa96@gmail.com";

    @Autowired
    private emailService mailService;

    @PostMapping(value = "/sendMail")
    @ResponseStatus(value = CREATED)
    public ResponseEntity<?> create(@RequestBody Email mail){
        Map<String,Object> response =new HashMap<String, Object>();
        try {
            String message ="Datos de contacto: "
                    + "\nNombre: " + mail.getName()
                    + "\nE-mail: " + mail.getMail()
                    + "\nMensaje: "+ mail.getBody();
            //MAIL PARA DANI
            mailService.sendMail(
                    CORREOPROPERTIES
                    ,CORREOREENVIO1
                    ,mail.getSubject()
                    ,message);
            //MAIL PARA PAUL
            mailService.sendMail(
                    CORREOPROPERTIES
                    ,CORREOREENVIO2
                    ,mail.getSubject()
                    ,message);

        }catch(DataAccessException e) {
            response.put("mensaje","Error al realizar el contacto");
            response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response, INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje","Contacto realizado con éxito!");
        return new ResponseEntity<Map<String,Object>>(response, OK);
    }

}
