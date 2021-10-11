package br.com.sabium.enumeration.pessoa;

import br.com.sabium.controller.exception.GraduacaoDontExistException;

public enum Status {

	PENDENTE("Pendente"), APROVADA("Aprovada"), CANCELADA("Cancelada");
	
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
