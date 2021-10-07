package br.com.sabium.controller.exception;

public class DisciplinaAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DisciplinaAlreadyExistsException () {
		super("Disciplina ja existe!");
	}

}
