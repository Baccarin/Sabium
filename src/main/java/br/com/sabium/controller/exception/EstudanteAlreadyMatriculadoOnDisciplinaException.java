package br.com.sabium.controller.exception;

public class EstudanteAlreadyMatriculadoOnDisciplinaException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EstudanteAlreadyMatriculadoOnDisciplinaException () {
		super("Estudante ja esta matriculado nessa disciplina!");
	}

}
