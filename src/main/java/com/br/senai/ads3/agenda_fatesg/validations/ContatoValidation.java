/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.br.senai.ads3.agenda_fatesg.validations;

import com.br.senai.ads3.agenda_fatesg.domains.Contato;
import com.br.senai.ads3.agenda_fatesg.exceptions.CoreException;
import com.br.senai.ads3.agenda_fatesg.exceptions.ExceptionValidationCampo;
import com.br.senai.ads3.agenda_fatesg.exceptions.ExceptionValidationRegra;
import com.br.senai.ads3.agenda_fatesg.repositories.IContatoRepository;

/**
 *
 * @author Clayton
 */
public class ContatoValidation implements IContatoValidation {

    private final IContatoRepository repository;
    
    public ContatoValidation(final IContatoRepository repository){
        this.repository = repository;
    }

    /**
     *
     * @param contato
     * @throws Exception
     */
    @Override
    public void validaCampo(Contato contato) throws ExceptionValidationCampo {
        if (contato == null) {
            throw new ExceptionValidationCampo("Objeto Contato", "O contato não pode ser nulo.", "Falha de cadastro.", "error");
        }
        if (contato.getNome() == null || contato.getNome().trim().length() < 5) {
            throw new ExceptionValidationCampo("Nome", "O nome deve ter pelo menos 5 caracteres.", "Falha de validação", "warning");
        }
        if (contato.getEmail() == null || !contato.getEmail().contains("@")) {
            throw new ExceptionValidationCampo("E-mail", "Informe um e-mail válido.", "Falha de validação", "warning");
        }
        if (contato.getTelefone() == null || contato.getTelefone().trim().isEmpty()) {
            throw new ExceptionValidationCampo("Telefone", "O telefone é obrigatório.", "Falha de validação", "warning");
        }
    }

    @Override
    public void validaRegraInserir(Contato contato) throws ExceptionValidationRegra {
        String regra = "Não permitido a duplicação de registro.";
        try {
            if(this.repository.contatoExiste(contato)){
                throw new ExceptionValidationRegra(regra, "O contato ".concat(contato.getNome()).concat(" já está cadastrado."), "Falha ao inserir.", "");
            }
        } catch (CoreException ex) {
            throw new ExceptionValidationRegra(regra, ex.getMessage(), ex.getTitulo(), ex.getIcone());
        }
    }

    @Override
    public void validaRegraAlterar(Contato contato) throws ExceptionValidationRegra {
        String regra = "Validação de existência.";
        try {
            if (!this.repository.contatoExiste(contato)) {
                throw new ExceptionValidationRegra(regra, "O contato " + contato.getNome() + " não foi encontrado para alteração.", "Falha ao alterar.", "error");
            }
        } catch (CoreException ex) {
            throw new ExceptionValidationRegra(regra, ex.getMessage(), ex.getTitulo(), ex.getIcone());
        }
    }

    @Override
    public void validaRegraAtivar(Contato contato) throws ExceptionValidationRegra {
        // Regra simples: para ativar, o contato deve existir na base (mesmo que inativo)
    }

    @Override
    public void validaRegraInativar(Contato contato) throws ExceptionValidationRegra {
        // Regra simples: para inativar, o contato deve existir
    }
    
}
