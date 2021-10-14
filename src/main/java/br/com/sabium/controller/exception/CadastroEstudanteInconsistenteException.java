package br.com.sabium.controller.exception;

public class CadastroEstudanteInconsistenteException  extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CadastroEstudanteInconsistenteException () {
		super("Para cadastrar um estudante, é necessários todos os campos preenchidos!");
	}
}
