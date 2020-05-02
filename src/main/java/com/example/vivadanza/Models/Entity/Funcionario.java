package com.example.vivadanza.Models.Entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Funcionario")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_funcionario;
    private String Nombres;
    private String Apellidos;
    private String Cargo;
    private String leyenda;
    private boolean estado_funcionario;
    private String foto;

}
