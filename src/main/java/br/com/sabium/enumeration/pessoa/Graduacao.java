package br.com.sabium.enumeration.pessoa;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.sabium.controller.exception.GraduacaoDontExistException;

public enum Graduacao implements Serializable{

    @JsonProperty("Graduacao")
	GRADUACAO("Graduacao"),
	
    @JsonProperty("Mestre")
	MESTRE("Mestre"),
	
    @JsonProperty("Doutor(a)")
	DOUTORADO("Doutor(a)"),
	
    @JsonProperty("PHD")
	PHD("PHD");

	private final String descricao;

	private Graduacao(String graduacao) {
		this.descricao = graduacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static Graduacao converte(String graduacao) {
    	try {
    		return Graduacao.valueOf(graduacao.toUpperCase());
    	}catch (Exception e) {
    		throw new GraduacaoDontExistException();
    	}
	}
}
