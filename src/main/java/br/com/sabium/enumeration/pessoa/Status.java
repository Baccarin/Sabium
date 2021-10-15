package br.com.sabium.enumeration.pessoa;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.sabium.controller.exception.GraduacaoDontExistException;

public enum Status {

	@JsonProperty("Pendente")
	PENDENTE("Pendente"), 
	
	@JsonProperty("Aprovada")
	APROVADA("Aprovada"), 
	
	@JsonProperty("Cancelada")
	CANCELADA("Cancelada");
	
	private final String descricao;

	private Status(String status) {
		this.descricao = status;
	}

	public String getDescricao() {
		return descricao;
	}

	public static Status converte(String status) {
    	try {
    		return Status.valueOf(status.toUpperCase());
    	}catch (Exception e) {
    		throw new GraduacaoDontExistException();
    	}
	}
	
}
