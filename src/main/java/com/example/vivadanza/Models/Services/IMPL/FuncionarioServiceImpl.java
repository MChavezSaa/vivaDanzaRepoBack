package com.example.vivadanza.Models.Services.IMPL;

import com.example.vivadanza.Models.DAO.IFuncionarioDao;
import com.example.vivadanza.Models.Entity.Funcionario;
import com.example.vivadanza.Models.Services.Interface.IFuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioServiceImpl implements IFuncionarioService {
    @Autowired
    private IFuncionarioDao funcionarioDao;

    @Transactional
    public List<Funcionario> findAll() {
        return funcionarioDao.findAll();
    }

    @Transactional
    public Funcionario save(Funcionario funcionario) {
        return funcionarioDao.save(funcionario);
    }

    @Transactional
    public Optional<Funcionario> findOne(long id) {
        return funcionarioDao.findById(id);
    }

    @Transactional
    public void delete(Funcionario funcionario) {
        funcionario.setEstado_funcionario(false);
        funcionarioDao.save(funcionario);
    }

    @Transactional
    public void deletebyID(long id) {
        Funcionario funcBuscado = funcionarioDao.findById(id).get();
        funcBuscado.setEstado_funcionario(false);
        funcionarioDao.save(funcBuscado);
    }

}
