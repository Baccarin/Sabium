package br.com.sabium.controller.exception;

public class MatriculaCanceladaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MatriculaCanceladaException () {
		super("Impossivel alterar uma matricula cancelada!");
	}
	
}
