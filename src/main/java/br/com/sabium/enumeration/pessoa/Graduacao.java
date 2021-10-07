package br.com.sabium.enumeration.pessoa;

import br.com.sabium.controller.exception.GraduacaoDontExistException;

public enum Graduacao {

	GRADUACAO("Gradua��o"), MESTRADO("Mestre"), DOUTORADO("Doutor(a)"), PHD("PHD");

	private final String descricao;

	private Graduacao(String graduacao) {
		this.descricao = graduacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static Graduacao converte(String graduacao) {
    	try {
    		return Graduacao.valueOf(graduacao);
    	}catch (Exception e) {
    		throw new GraduacaoDontExistException();
    	}
	}
}
