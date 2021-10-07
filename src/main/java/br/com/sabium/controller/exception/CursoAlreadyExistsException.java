package br.com.sabium.controller.exception;

public class CursoAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CursoAlreadyExistsException () {
		super("Curso ja existe!");
	}

}
