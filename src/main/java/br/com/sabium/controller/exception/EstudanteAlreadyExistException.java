package br.com.sabium.controller.exception;

public class EstudanteAlreadyExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public EstudanteAlreadyExistException () {
		super("Estudante ja existe!");
	}
}
