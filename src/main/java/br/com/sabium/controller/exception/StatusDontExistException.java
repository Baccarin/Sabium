package br.com.sabium.controller.exception;

public class StatusDontExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public StatusDontExistException () {
		super("Status não existe!");
	}
	
}
