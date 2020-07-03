package com.example.vivadanza.Models.Services.Interface;

import com.example.vivadanza.Models.Entity.Contacto;

public interface IContactoService {
    Contacto save(Contacto contacto);
    Contacto findbyId(Long id);
}
