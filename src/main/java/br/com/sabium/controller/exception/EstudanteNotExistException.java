package br.com.sabium.controller.exception;

public class EstudanteNotExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public EstudanteNotExistException () {
		super("Estudante nao existe!");
	}
	
}
