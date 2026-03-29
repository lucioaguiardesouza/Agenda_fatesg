
package com.br.senai.ads3.agenda_fatesg.controllers;

import java.nio.file.Path;
import java.util.List;

import com.br.senai.ads3.agenda_fatesg.domains.Contato;
import com.br.senai.ads3.agenda_fatesg.dtos.Response;
import com.br.senai.ads3.agenda_fatesg.exceptions.CoreException;
import com.br.senai.ads3.agenda_fatesg.exceptions.ExceptionValidationCampo;
import com.br.senai.ads3.agenda_fatesg.exceptions.ExceptionValidationRegra;
import com.br.senai.ads3.agenda_fatesg.services.ContatoService;
import com.br.senai.ads3.agenda_fatesg.services.IContatoService;


public class ContatoController implements IContatoCadastroController, IContatoListaController {
    
    private final IContatoService service;

    public ContatoController() {
       this.service = new ContatoService();
    }    

    public ContatoController(Path storage) {
        this.service = new ContatoService(storage);
    }
    
    @Override
    public Response criar(Contato dto){
        try {
            if(this.service.inserir(dto)){
                return Response.responseOk(dto);
            } else {
                return Response.responseOk(null);
            }
        } catch (ExceptionValidationCampo ex) {
            return Response.responseErroCampo(ex);
        } catch (ExceptionValidationRegra ex) {
            return Response.responseErroRegra(ex);
        }
    }

    @Override
    public Response alterar(String originalName, Contato dto) {
        try {
            if(this.service.alterar(dto)){
                return Response.responseOk(dto);
            } else {
                return Response.responseOk(null);
            }
        } catch (ExceptionValidationCampo ex) {
            return Response.responseErroCampo(ex);
        } catch (ExceptionValidationRegra ex) {
            return Response.responseErroRegra(ex);
        }
    }

    @Override
    public Response listarTodos()  {
        try {
            return Response.responseOk(this.service.buscarTodos());
        } catch (CoreException ex) {
            return Response.responseErro(ex);
        }
    }
    @Override
    public Response listarTodosAtivos(){
        try {
            return Response.responseOk(this.service.buscarTodosAtivos());
        } catch (CoreException ex) {
            return Response.responseErro(ex);
        }
    }
    @Override
    public Response listaTodosInativos() {
        try {
            return Response.responseOk(this.service.buscarTodosInativos());
        } catch (CoreException ex) {
            return Response.responseErro(ex);
        }
    }

    @Override
    public Response inativarPorNome(String name) {
        try {
            if (this.service.excluir(name)) {
                return Response.responseOk(true);
            } else {
                return Response.responseOk(false);
            }
        } catch (ExceptionValidationCampo ex) {
            return Response.responseErroCampo(ex);
        } catch (ExceptionValidationRegra ex) {
            return Response.responseErroRegra(ex);
        }
    }

    @Override
    public Response buscarPorNome(String name) {
        try {
            List<Contato> filtered = this.service.listarPorNome(name);
            return Response.responseOk(filtered);
        } catch (CoreException ex) {
            return Response.responseErro(ex);
        }
    }
}
