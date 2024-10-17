package br.com.zbhousereservas.dto;

import br.com.zbhousereservas.entities.Usuario;

public record DadosAutenticacao(String login, String senha) {

    public DadosAutenticacao(Usuario usuario){
        this(usuario.getLogin(), usuario.getSenha());
    }
}
