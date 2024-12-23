package br.com.zbhousereservas.dto;

import br.com.zbhousereservas.entities.Autenticacao;

public record DadosAutenticacao(String login, String senha) {

    public DadosAutenticacao(Autenticacao autenticacao){
        this(autenticacao.getLogin(), autenticacao.getSenha());
    }
}
