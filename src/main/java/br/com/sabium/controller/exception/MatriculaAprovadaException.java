package br.com.sabium.controller.exception;

public class MatriculaAprovadaException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MatriculaAprovadaException () {
		super("Impossivel alterar uma matricula aprovada!");
	}
}
