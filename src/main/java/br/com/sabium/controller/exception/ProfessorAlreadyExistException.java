package br.com.sabium.controller.exception;

public class ProfessorAlreadyExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public ProfessorAlreadyExistException () {
		super("Professor ja existe!");
	}
}
