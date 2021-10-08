package br.com.sabium.controller.exception;

public class CursoWithDisciplinasAssociateException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CursoWithDisciplinasAssociateException () {
		super("Impossivel excluir um cuso com disciplinas associadas!");
	}

}
