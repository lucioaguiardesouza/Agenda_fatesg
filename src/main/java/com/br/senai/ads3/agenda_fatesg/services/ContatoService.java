/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.br.senai.ads3.agenda_fatesg.services;

import com.br.senai.ads3.agenda_fatesg.domains.Contato;
import com.br.senai.ads3.agenda_fatesg.exceptions.CoreException;
import com.br.senai.ads3.agenda_fatesg.exceptions.ExceptionValidationCampo;
import com.br.senai.ads3.agenda_fatesg.exceptions.ExceptionValidationRegra;
import com.br.senai.ads3.agenda_fatesg.repositories.ContatoRepository;
import com.br.senai.ads3.agenda_fatesg.repositories.IContatoRepository;
import com.br.senai.ads3.agenda_fatesg.validations.ContatoValidation;
import com.br.senai.ads3.agenda_fatesg.validations.IContatoValidation;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CLAYTON.MARQUES
 */
public class ContatoService implements IContatoService {

    private final IContatoRepository repository;
    private final IContatoValidation validation;

    public ContatoService() {
        this.repository = new ContatoRepository();
        this.validation = new ContatoValidation(this.repository);
    }

    public ContatoService(Path storage) {
        this.repository = new ContatoRepository(storage);
        this.validation = new ContatoValidation(this.repository);
    }

    @Override
    public boolean inserir(Contato contato) throws ExceptionValidationCampo, ExceptionValidationRegra {
        this.validation.validaCampo(contato);
        this.validation.validaRegraInserir(contato);
        try {
            return this.repository.inserir(contato);
        } catch (CoreException ex) {
            throw new ExceptionValidationRegra("Inserção", ex.getMessage(), ex.getTitulo(), ex.getIcone());
        }
    }

    @Override
    public boolean alterar(Contato contato) throws ExceptionValidationCampo, ExceptionValidationRegra {
        this.validation.validaCampo(contato);
        this.validation.validaRegraAlterar(contato);
        try {
            return this.repository.alterar(contato);
        } catch (CoreException ex) {
            throw new ExceptionValidationRegra("Alteração", ex.getMessage(), ex.getTitulo(), ex.getIcone());
        }
    }

    @Override
    public boolean excluir(Contato contato) throws ExceptionValidationCampo, ExceptionValidationRegra {
        try {
            if (this.contatoExiste(contato)) {
                return this.repository.desativar(contato);
            } else {
                throw new ExceptionValidationRegra("Inativação", "Este contato não existe ou já está inativo.", "Atenção", "warning");
            }
        } catch (CoreException ex) {
            throw new ExceptionValidationRegra("Erro Técnico", ex.getMessage(), ex.getTitulo(), ex.getIcone());
        }
    }
    
    @Override
    public boolean excluir(String nome) throws ExceptionValidationCampo, ExceptionValidationRegra {
        try {
            Contato contato = this.buscarPorNome(nome);
            if (contato != null) {
                return excluir(contato);
            }
            return false;
        } catch (CoreException ex) {
            throw new ExceptionValidationRegra("Exclusão", ex.getMessage(), ex.getTitulo(), ex.getIcone());
        }
    }
    
    @Override
    public List<Contato> listarPorNome(String name) throws CoreException {
        List<Contato> all = this.buscarTodos();
        List<Contato> filtered = new ArrayList<>();
        for (Contato c : all) {
            if (c.getNome() != null && c.getNome().toLowerCase().contains(name.toLowerCase())) {
                filtered.add(c);
            }
        }
        return filtered;
    }
    
    @Override
    public Contato buscarPorNome(String name) throws CoreException {
        List<Contato> all = this.buscarTodos();
        for (Contato c : all) {
            if (c.getNome() != null && c.getNome().toLowerCase().contains(name.toLowerCase())) {
                return c;
            }
        }
        return null;
    }

    @Override
    public List<Contato> buscarTodos() throws CoreException {
        return this.repository.buscarTodos();
    }
    
    @Override
    public List<Contato> buscarTodosAtivos() throws CoreException {
        return this.repository.buscarTodos(true);
    }
    
    @Override
    public List<Contato> buscarTodosInativos() throws CoreException {
        return this.repository.buscarTodos(false);
    }

    @Override
    public boolean contatoExiste(Contato contato) throws CoreException {
        return this.repository.contatoExiste(contato);
    }

    @Override
    public boolean reativaContato(Contato contato) throws CoreException {
        return this.repository.reativar(contato);
    }


}
