package br.com.sabium.controller.exception;

public class TurnoDontExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public TurnoDontExistException () {
		super("Turno n�o existe!");
	}
	
}
