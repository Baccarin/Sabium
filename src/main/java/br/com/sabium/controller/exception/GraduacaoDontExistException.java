package br.com.sabium.controller.exception;

public class GraduacaoDontExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public GraduacaoDontExistException () {
		super("Gradua��o n�o existe!");
	}
	
}
