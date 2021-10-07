package br.com.sabium.controller.exception;

public class DisciplinaNotExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public DisciplinaNotExistException () {
		super("Disciplina nao existe!");
	}
	
}
