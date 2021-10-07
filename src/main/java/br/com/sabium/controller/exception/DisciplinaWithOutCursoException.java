package br.com.sabium.controller.exception;

public class DisciplinaWithOutCursoException  extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DisciplinaWithOutCursoException () {
		super("Impossível cadastrar uma disciplina sem um curso");
	}
	
}
